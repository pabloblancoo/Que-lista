package grupomoviles.quelista.onlineDatabase;


import com.annimon.stream.Stream;

public class QueryBuilder {

    private String document;

    public String findProduct(String codigo) {
        return "q={\"codigo\":\"" + codigo + "\"}";
    }

    public String findProducts(String... barcodes) {
        StringBuilder sb = new StringBuilder("q={\"codigo\":{$in:[");

        Stream.of(barcodes).forEach(s -> sb.append("\"").append(s).append("\","));

        return sb.deleteCharAt(sb.length() - 1).append("]}}").append("&s={\"descripcion\":1}").toString();
    }

    public QueryBuilder(String document) {
        this.document = document;
    }

    /**
     * Specify your database name here
     * @return
     */
    public String getDatabaseName() {
            return "quelista";
    }

    /**
     * Specify your MongoLab API here
     * @return
     */
    public String getApiKey() {
        return "YPWrvhNBADCTn2my6G5g_0yjum-KnGX-";
    }

    /**
     * This constructs the URL that allows you to manage your database,
     * collections and documents
     * @return
     */
    public String getBaseUrl()
    {
        return "https://api.mongolab.com/api/1/databases/"+getDatabaseName()+"/collections/";
    }

    /**
     * Completes the formating of your URL and adds your API key at the end
     * @return
     */
    public String docApiKeyUrl()
    {
        return "apiKey="+getApiKey();
    }

    /**
     * Returns the docs101 collection
     * @return
     */
    public String documentRequest()
    {
        return document;
    }

    public String findPostURL(String query) {
        return getBaseUrl()+documentRequest()+"?"+query+"&"+docApiKeyUrl();
    }
}