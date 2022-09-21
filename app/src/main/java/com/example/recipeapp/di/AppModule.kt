package com.example.recipeapp.di

import com.example.recipeapp.BuildConfig
import com.example.recipeapp.data.repositories.RecipeRepository
import com.example.recipeapp.data.repositories.RecipeRepositoryImp
import com.example.recipeapp.data.source.remote.RecipeNetworkDataSourceImp
import com.example.recipeapp.data.source.remote.RecipeRemoteDataSource
import com.example.recipeapp.data.source.remote.api.RecipeApi
import com.example.recipeapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
            val originalHttpUrl = chain.request().url()
            val url =
                originalHttpUrl.newBuilder().addQueryParameter("apiKey", BuildConfig.API_KEY)
                    .build()
            request.url(url)
            return@addInterceptor chain.proceed(request.build())
        }
        .build()

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(Constants.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()

    @Provides
    @Singleton
    fun provideRecipeApi(retrofit: Retrofit): RecipeApi = retrofit.create(RecipeApi::class.java)

    @Provides
    @Singleton
    fun provideRecipeRemoteDataSource(
        recipeApi: RecipeApi
    ): RecipeRemoteDataSource =
        RecipeNetworkDataSourceImp(recipeApi)

    @Provides
    @Singleton
    fun provideDatabaseReference(): DatabaseReference =
        FirebaseDatabase.getInstance().getReference("users")

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth =
        FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore =
        FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideCollectionReference(firebaseFirestore: FirebaseFirestore): CollectionReference =
        firebaseFirestore.collection(Constants.USER_COLLECTION)


    @Provides
    @Singleton
    fun provideRecipeRepository(
        recipeRemoteDataSource: RecipeRemoteDataSource
    ): RecipeRepository =
        RecipeRepositoryImp(recipeRemoteDataSource)
}