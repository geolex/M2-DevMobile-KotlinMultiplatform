package com.adrien.kmp_multiplatform.di

import com.amonteiro.a09_ynov_lyon_android.data.remote.WeatherApiDataSource
import com.amonteiro.a09_ynov_lyon_android.presentation.MainViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

//Si besoin du contexte, pour le passer en paramètre au lancement de Koin
fun initKoin(appDeclaration: KoinAppDeclaration = {}) {
    startKoin {
        appDeclaration()
        modules(apiModule, viewModelModule)
    }
}
// Version pour iOS et Desktop
fun initKoin() = initKoin {}

//------------------------
//DECLARATION DES MODULES
//------------------------
val apiModule = module {
    //Création d'un singleton pour le client HTTP
    single {
        HttpClient {
            install(Logging) {
                //(import io.ktor.client.plugins.logging.Logger)
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
                level = LogLevel.INFO  // TRACE, HEADERS, BODY, etc.
            }
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true }, contentType = ContentType.Any)
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 5000
            }
        }
    }

    //V1 : On crée à la main les attentes de WeatherApiDataSource
    //single {
    //    val httpClient = HttpClient {...}
    //    WeatherApiDataSource(httpClient)
    //}
    //V2 : Get() injectera les objets déjà connus par koin, ici le HttpClient
    //single { WeatherApiDataSource(get()) }

    //V3 : Si on a que des get()
    singleOf(::WeatherApiDataSource)
}

//Version spécifique au ViewModel
val viewModelModule = module {

    //V1 : Si on veut ajouter manuellement certain paramètre
    //viewModel { MainViewModel(get(), Dispatchers.IO) }
    //V2 On déclare un Dispatchers à koin
    //factory { Dispatchers.IO }

    viewModelOf(::MainViewModel)
}