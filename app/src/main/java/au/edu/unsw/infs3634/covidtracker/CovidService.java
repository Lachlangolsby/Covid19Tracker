package au.edu.unsw.infs3634.covidtracker;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CovidService {

    @GET("summary")
   Call<Response> getResponse();
}


