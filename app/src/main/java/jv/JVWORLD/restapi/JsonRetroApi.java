package jv.JVWORLD.restapi;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface JsonRetroApi {

//    @GET("{uriPath}")
//    Call<List<PostModel>> getPost(@Path("uriPath") String pathApi, @Query("userId") int userId);
//    Call<List<PostModel>> getPost(@Path("uriPath") String pathApi, @QueryMap Map<String,String> stringMap);
//    Call<List<PostModel>> getPost(@Path("uriPath") String pathApi);
    @GET
    Call<List<PostModel>> getPost(@Url  String pathApi, @QueryMap Map<String,String> stringMap);
    @GET
    Call<List<PostModel>> getPost(@Url  String pathApi);


}
