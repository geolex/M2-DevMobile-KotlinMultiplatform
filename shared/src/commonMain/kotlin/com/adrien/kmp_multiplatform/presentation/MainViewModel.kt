package com.amonteiro.a09_ynov_lyon_android.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adrien.kmp_multiplatform.di.initKoin
import com.amonteiro.a09_ynov_lyon_android.data.remote.WeatherApiDataSource
import com.amonteiro.a09_ynov_lyon_android.domain.model.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform
import kotlin.time.Duration.Companion.milliseconds


suspend fun main() {
    initKoin()
    val viewModel = MainViewModel(KoinPlatform.getKoin().get<WeatherApiDataSource>())
    //viewModel.loadWeathers("")
    viewModel.loadWeathers("Paris")
    //attente
    while (viewModel.runInProgress.value) {
        delay(500)
    }
    //Affichage de la liste ou du message d'erreur
    println("List : ${viewModel.dataList.value}")
    println("ErrorMessage : ${viewModel.errorMessage.value}")

}

class MainViewModel(val weatherApiDataSource: WeatherApiDataSource) : ViewModel() {

    val dataList = MutableStateFlow(emptyList<Weather>())
    val runInProgress = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

    init {
        loadWeathers("Nice")
    }


    fun loadWeathers(cityName: String) {

        runInProgress.value = true

        viewModelScope.launch(Dispatchers.IO){

            try {
                if(cityName.length < 3){
                    throw Exception("Il faut au moins 3 caractères")
                }
                dataList.value = weatherApiDataSource.loadWeathers(cityName)
            }
            catch (e: Exception) {
                e.printStackTrace()
                updateError(e.message ?: "Une erreur est survenue")
            }
            finally {
                runInProgress.value = false
            }
        }
    }
    private fun updateError(text:String){
        errorMessage.value = text
        viewModelScope.launch(Dispatchers.IO){
            delay(3000.milliseconds)
            errorMessage.value = ""
        }
    }
}