package com.epam.esm.dao.impl.builder;


import java.util.Map;

/**
 * The class that creates requests using parameters.
 */
public class GiftCertificateRequestBuilder {

    private GiftCertificateRequestBuilder(){}

    /**
     * Creates requests.
     *
     * @param params the parameters of request
     * @return the request
     */
    public static String createGetAllRequest(Map<String, String> params){
        String GET_ALL_CERTIFICATES_SQL = "SELECT gift_certificate.id, gift_certificate.name, gift_certificate.description, "
                + "gift_certificate.price, gift_certificate.duration, gift_certificate.create_date, gift_certificate.last_update_date FROM gift_certificate";

        boolean isWhereContains = false;
        if (!params.get("tag").equals("")){
            GET_ALL_CERTIFICATES_SQL = GET_ALL_CERTIFICATES_SQL.concat(" JOIN gift_certificate_tag ON gift_certificate.id = " +
                    "gift_certificate_tag.certificate_id JOIN tag ON gift_certificate_tag.tag_id = tag.id WHERE tag.name " +
                    "LIKE \"%" + params.get("tag") + "%\" ");
            isWhereContains = true;
        }

        if (!params.get("search").equals("")){
            if (isWhereContains){
                GET_ALL_CERTIFICATES_SQL = GET_ALL_CERTIFICATES_SQL.concat(" AND ");
            } else {
                GET_ALL_CERTIFICATES_SQL = GET_ALL_CERTIFICATES_SQL.concat(" WHERE ");
            }
            GET_ALL_CERTIFICATES_SQL = GET_ALL_CERTIFICATES_SQL.concat(" gift_certificate.name LIKE \"%"
                    + params.get("search") + "%\" ");
        }

        if (!params.get("sort").equals("")){
            GET_ALL_CERTIFICATES_SQL = GET_ALL_CERTIFICATES_SQL.concat(" ORDER BY gift_certificate.name " + params.get("sort"));
        }
        return GET_ALL_CERTIFICATES_SQL;
    }
}
