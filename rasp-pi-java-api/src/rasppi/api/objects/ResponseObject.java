package rasppi.api.objects;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpResponse;


import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * ResponseObject:
 *      Class that represents the response from Request.
 */
public class ResponseObject {
    private Integer Status;
    private Map<String, String> Header;
    private Map<String, Object> Body;
    private ObjectMapper Mapper = new ObjectMapper();

    public ResponseObject() {
        this.setStatus(0);
        this.setHeader(null);
        this.setBody(null);
    }

    public ResponseObject(Integer status){
        this.setStatus(status);
        this.setHeader(null);
        this.setBody(null);
    }

    public ResponseObject(Integer status, Map<String, String> header){
        this.setStatus(status);
        this.setHeader(header);
        this.setBody(null);
    }

    public ResponseObject(Integer status, Map<String, String> header, Map<String, Object> body){
        this.setStatus(status);
        this.setHeader(header);
        this.setBody(body);
    }

    public ResponseObject(HttpResponse response) {
        this.setStatus(response.getStatusCode());

        HttpHeaders httpHeaders = response.getHeaders();
        Iterator iterator = httpHeaders.keySet().iterator();
        Map<String, String> header = new HashMap<>();
        while (iterator.hasNext()) {
            String key = (String) iterator.next();
            header.put(key, httpHeaders.getHeaderStringValues(key).toString());
        }
        this.setHeader(header);

        try {
            this.setBody(Mapper.readValue(response.getContent(), Map.class));
        } catch (IOException e) {
            System.err.println(e.getMessage().toString());
        }
    }

    public Integer getStatus() {
        return Status;
    }

    public void setStatus(Integer status) {
        Status = status;
    }

    public Map<String, String> getHeader() {
        return Header;
    }

    public void setHeader(Map<String, String> header) {
        Header = header;
    }

    public Map<String, Object> getBody() {
        return Body;
    }

    public void setBody(Map<String, Object> body) {
        Body = body;
    }

    public Map<String, Object> getBodyData(){
        Map<String, Object> content = (Map<String, Object>)Body.get("content");
        return (Map<String, Object>)content.get("data");
    }

    /**
     * convertStreamtoMap:
     *      A helper method that converts an InputStream to a Map<String, String> object.
     *
     * @param is
     * @return
     */
    private Map<String, String> convertStreamToMap(InputStream is) {
        Map<String, String> map = new HashMap<>();
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");

        String string = s.hasNext() ? s.next() : "";
        string = string.substring(2, string.lastIndexOf('}'));
        String[] pairs = string.split(",");
        for(int i=0;i<pairs.length;i++){
            String pair = pairs[i];
            String[] keyValue = pair.split(":");
            String key = keyValue[0].substring(1, keyValue[0].length() - 1);
            String value = keyValue[1];
            if( value.charAt(0) == '"'){
                value = value.substring(1, value.length() - 1);
            }
            map.put(key, value);
        }
        return map;
    }

}
