package com.davidecirillo.dashboard.data.api;


import retrofit.http.POST;
import retrofit.http.Path;
import rx.Observable;

/**
 * Created by davidecirillo on 26/04/16.
 */
public interface PIApi {

    @POST("/api/ToggleLight/{led_name}/{gpio_number}")
    Observable<String> toggleLight(
            @Path("led_name") String led_name,
            @Path("gpio_number") int gpio_number
    );

    @POST("/api/ToggleBlind/{led_name}/{gpio_number}")
    Observable<String> toggleBlind(
            @Path("led_name") String led_name,
            @Path("gpio_number") int gpio_number
    );

}