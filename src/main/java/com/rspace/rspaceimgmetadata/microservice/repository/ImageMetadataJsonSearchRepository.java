package com.rspace.rspaceimgmetadata.microservice.repository;

import com.rspace.rspaceimgmetadata.microservice.Model.ImageMetadataEmbeddedKey;
import com.rspace.rspaceimgmetadata.microservice.Model.ImageMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageMetadataJsonSearchRepository extends JpaRepository<ImageMetadataEntity, ImageMetadataEmbeddedKey> {

    String queryPartSelect =
            "SELECT " +
            "  CAST(" +          // The cast is necessary because otherwise java cannot interprete the json result as string
            "    json_object(" +
            "      'id', i.id," +
            "      'customer_id', i.customer_id," +
            "      'rspace_image_id', rspace_image_id, " +
            "      'image_version', image_version, " +
            "      'user_id', i.user_id," +
            "      'metadata', " +
            "       json_arrayagg(" +
            "         json_object(" +
            "           search.key_path, " +
            "           json_extract(i.metadata, search.key_path)" +
            "        ) " +
            "      ) " +
            "    )" +
            "    as char" +
            "  ) as matches ";

    /**
     * Param:  :term
     */
    String queryPartFrom =
            "FROM   " +
            "  image_metadata i," +
            "    JSON_TABLE(" +
                    //JSON_MERGE is necessary because otherwise single results are not in an (1-element) array and
                    // will not be considered from the json table operator
            "      JSON_MERGE(" +
            "        '[]', " +
            "        JSON_SEARCH(i.metadata,'all', :term)" +
            "        )," +
            "    \"$[*]\"" +
            "        COLUMNS(key_path char(50) PATH '$')" +
            "  ) as search ";

    /**
     * Param:  :term
     */
    String queryPartWhereNotNull =
            "where" +
            "  JSON_SEARCH(i.metadata,'all', :term) IS NOT NULL ";

    /**
     * Param:  :jsonKeys
     */
    String queryPartWhereSelectedKeys =
            "AND search.key_path IN (" +
            "  SELECT" +
            "    tar.target_keys" +
            "  FROM" +
            "    JSON_TABLE(" +
            "      :jsonKeys," +
            "      \"$[*]\"" +
            "      COLUMNS(target_keys char(50) PATH '$')" +
            "    ) as tar" +
            "  )";

    /**
     * Param:  :jsonUsers
     */
    String queryPartWhereSelectedUsers =
            "AND i.user_id IN ( " +
            "  SELECT" +
            "    u.uid" +
            "  FROM" +
            "    JSON_TABLE(" +
            "      :jsonUsers," +
			"	   \"$[*]\"" +
            "      COLUMNS(uid char(50) PATH '$')" +
            "    ) as u" +
		    "  )";

    String queryPartGroupBy = "GROUP BY i.customer_id, rspace_image_id, image_version";

    /***    Querys    ***/

    @Query(value = queryPartSelect + queryPartFrom + queryPartWhereNotNull + queryPartGroupBy, nativeQuery = true)
    String[] searchInAll(@Param("term") String searchTerm);


    @Query(value = queryPartSelect + queryPartFrom + queryPartWhereNotNull + queryPartWhereSelectedUsers + queryPartGroupBy, nativeQuery = true)
    String[] searchInAllKeysOfUsers(@Param("term") String searchTerm, @Param("jsonUsers") String jsonUsers);


    @Query(value = queryPartSelect + queryPartFrom + queryPartWhereNotNull + queryPartWhereSelectedKeys + queryPartWhereSelectedUsers + queryPartGroupBy, nativeQuery = true)
    String[] searchInKeysOfUsers(@Param("term") String searchTerm, @Param("jsonKeys")String jsonTarKeyArr, @Param("jsonUsers") String jsonUsers);


    @Query(value = queryPartSelect + queryPartFrom + queryPartWhereNotNull + queryPartWhereSelectedKeys + queryPartGroupBy, nativeQuery = true)
    String[] searchInKeysForAllUsers(@Param("term") String searchTerm, @Param("jsonKeys")String jsonTarKeyArr);

}
