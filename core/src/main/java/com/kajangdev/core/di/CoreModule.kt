package com.kajangdev.core.di

import androidx.room.Room
import com.kajangdev.core.data.Repository
import com.kajangdev.core.data.source.local.LocalDataSource
import com.kajangdev.core.data.source.local.room.MovieKitaDatabase
import com.kajangdev.core.data.source.remote.RemoteDataSource
import com.kajangdev.core.data.source.remote.network.ApiService
import com.kajangdev.core.domain.repository.IRepository
import com.kajangdev.core.utils.AppExecutors
import com.kajangdev.core.utils.Constant.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val databaseModule = module {
    factory { get<MovieKitaDatabase>().movieKitaDao() }
    single {
        Room.databaseBuilder(
                androidContext(),
                MovieKitaDatabase::class.java, "MovieKita.db"
        ).fallbackToDestructiveMigration().build()
    }
}

val networkModule = module {
    single {
        OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
    }
    single {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(get())
                .build()
        retrofit.create(ApiService::class.java)
    }
}

val repositoryModule = module {
    single { LocalDataSource(get()) }
    single { RemoteDataSource(get()) }
    factory { AppExecutors() }
    single<IRepository> {
        Repository(
                get(),
                get(),
                get()
        )
    }
}