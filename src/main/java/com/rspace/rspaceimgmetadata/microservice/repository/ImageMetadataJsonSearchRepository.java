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

    @Query(value = "SELECT \n" +
            "\ti.customer_id,\n" +
            "    rspace_image_id, \n" +
            "    image_version,\n" +
            "    CAST("+
            "    json_arrayagg(\n" +
            "\t\tjson_object(\n" +
            "\t\t\tsearch.key_path,          \n" +
            "\t\t\tjson_extract(i.metadata, search.key_path)\n" +
            "\t\t) \n" +
            "\t) as char) as matches\n" +
            "FROM   \n" +
            "\timage_metadata i,\n" +
            "    JSON_TABLE(\n" +
            "    JSON_SEARCH(i.metadata,'all',?1),\n" +
            "\t\t\"$[*]\"\n" +
            "        COLUMNS(key_path char(50) PATH '$')\n" +
            "\t) as search\n" +
            "where\n" +
            "\tJSON_SEARCH(i.metadata,'all',?1) IS NOT NULL\n" +
            "    AND search.key_path IN (\n" +
            "\t\tSELECT\n" +
            "\t\t\ttar.target_keys\n" +
            "        FROM\n" +
            "\t\t\tJSON_TABLE(\n" +
            "\t\t\t\t?2,\n" +
            "\t\t\t\t\"$[*]\"\n" +
            "\t\t\t\tCOLUMNS(target_keys char(50) PATH '$')\n" +
            "\t\t\t) as tar\n" +
            "\t\t)\n" +
            "\tGROUP BY i.customer_id, rspace_image_id, image_version", nativeQuery = true)
    String[] searchOverTargetKeys(String searchTerm, String jsonTarKeyArr);

    // Evtl fkt für prefixTerm, subTerm

}
