package com.davidecirillo.dashboard.data;

import com.davidecirillo.dashboard.BuildConfig;
import com.davidecirillo.dashboard.app.AppConfig;
import com.davidecirillo.dashboard.data.api.PIApi;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.logging.HttpLoggingInterceptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.RxJavaCallAdapterFactory;

/*
  ~ Copyright (c) 2014 Davide Cirillo
  ~
  ~     Licensed under the Apache License, Version 2.0 (the "License");
  ~     you may not use this file except in compliance with the License.
  ~     You may obtain a copy of the License at
  ~
  ~        http://www.apache.org/licenses/LICENSE-2.0
  ~
  ~     Unless required by applicable law or agreed to in writing, software
  ~     distributed under the License is distributed on an "AS IS" BASIS,
  ~     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  ~     See the License for the specific language governing permissions and
  ~     limitations under the License.
  ~     Come on, don't tell me you read that.
*/
public class RestService {

    private PIApi mLocalPiApi;
    private PIApi mRemotePiApi;

    private String mLocalUrl;
    private String mRemoteUrl;

    private static RestService mRestServiceInstance;

    public static RestService getInstance() {
        if(mRestServiceInstance == null)
            mRestServiceInstance = new RestService();

        return mRestServiceInstance;
    }

    public RestService() {
    }

    public void create(){
        final OkHttpClient client = new OkHttpClient();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        if (BuildConfig.DEBUG)
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        else
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);


        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(loggingInterceptor);
        interceptors.add(new LoggingInterceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .method(original.method(), original.body())
                        .build();

                return chain.proceed(request);
            }
        });

        client.networkInterceptors().addAll(interceptors);

        retrofit.Retrofit restAdapterLocal = new retrofit.Retrofit.Builder()
                .client(client)
                .baseUrl(AppConfig.http + mLocalUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        retrofit.Retrofit restAdapterRemote = new retrofit.Retrofit.Builder()
                .client(client)
                .baseUrl(AppConfig.http + mRemoteUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mLocalPiApi =  restAdapterLocal.create(PIApi.class);

        mRemotePiApi =  restAdapterRemote.create(PIApi.class);
    }

    public void updatePiRemoteAddress(String remoteAddress){
        mRemoteUrl = remoteAddress;
    }

    public void updatePiLocalAddress(String localAddress){
        mLocalUrl = localAddress;
    }

    public PIApi getLocalPiApi() {
        return mLocalPiApi;
    }

    public PIApi getRemotePiApi() {
        return mRemotePiApi;
    }
}
