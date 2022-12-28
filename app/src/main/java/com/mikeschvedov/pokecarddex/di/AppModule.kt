package com.mikeschvedov.pokecarddex.di

import android.content.Context
import com.mikeschvedov.pokecarddex.data.remote.PokeApi
import com.mikeschvedov.pokecarddex.repository.PokemonRepository
import com.mikeschvedov.pokecarddex.utils.Constants.POKEMON_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Singleton
    @Provides
    fun provideApplicationContext(@ApplicationContext appContext: Context) = appContext

    // --------- Repository --------- //

    @Singleton
    @Provides
    fun providePokemonRepository(
        api: PokeApi
    ) = PokemonRepository(api)

    // --------- Network --------- //

    @Singleton
    @Provides
    fun providePokeApi(
        httpClient: OkHttpClient
    ): PokeApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
           // .client(httpClient)
            .baseUrl(POKEMON_BASE_URL)
            .build()
            .create(PokeApi::class.java)
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    @Singleton
    @Provides
    fun provideOKHTTP(
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(loggingInterceptor)
        .build()
}