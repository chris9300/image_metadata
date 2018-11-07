package com.rspace.rspaceimgmetadata.microservice.repository;

import com.rspace.rspaceimgmetadata.microservice.Model.ImageMetadataEmbeddedKey;
import com.rspace.rspaceimgmetadata.microservice.Model.ImageMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ImageMetadataJsonSearchRepository extends JpaRepository<ImageMetadataEntity, ImageMetadataEmbeddedKey> {


    @Query(value = "SELECT * FROM image_metadata i WHERE JSON_CONTAINS(i.metadata, :#{#make21}, '$.\"Make\"') = 1", nativeQuery = true)
    //@Query(value = "SELECT * FROM image_metadata i WHERE JSON_EXTRACT(i.metadata, '$.\"Make\"') = ?1", nativeQuery = true)
    ImageMetadataEntity searchForMake(@Param("make21") String make21);

    @Query(value = "SELECT JSON_SEARCH(i.metadata, 'one', ?1) FROM image_metadata i", nativeQuery = true)
    String searchOverAll(String searchTerm);

    @Query(value =
            "SELECT " +
            "  CAST(" +          // The cast is necessary because otherwise java cannot interprete the json result as string
            "    json_object(" +
            "      'id', i.id," +
            "      'customer_id', i.customer_id," +
            "      'rspace_image_id', rspace_image_id, " +
            "      'image_version', image_version, " +
            "      'metadata', " +
            "       json_arrayagg(" +
            "         json_object(" +
            "           search.key_path, " +
            "           json_extract(i.metadata, search.key_path)" +
            "        ) " +
            "      ) " +
            "    )" +
            "    as char" +
            "  ) as matches " +
            "FROM   " +
            "  image_metadata i," +
            "    JSON_TABLE(" +
            "    JSON_SEARCH(i.metadata,'all',?1)," +
            "    \"$[*]\"" +
            "        COLUMNS(key_path char(50) PATH '$')" +
            "  ) as search " +
            "where" +
            "  JSON_SEARCH(i.metadata,'all',?1) IS NOT NULL" +
            "    AND search.key_path IN (" +
            "    SELECT" +
            "      tar.target_keys" +
            "        FROM" +
            "      JSON_TABLE(" +
            "        ?2," +
            "        \"$[*]\"" +
            "        COLUMNS(target_keys char(50) PATH '$')" +
            "      ) as tar" +
            "    )" +
            "  GROUP BY i.customer_id, rspace_image_id, image_version", nativeQuery = true)
    String[] searchOverTargetKeys(String searchTerm, String jsonTarKeyArr);

    // Evtl fkt für prefixTerm, subTerm

}
