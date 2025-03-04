package com.br.myapplication.ui.planets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.br.myapplication.data.dao.PlanetsDao
import com.br.myapplication.data.model.Planet
import com.br.myapplication.data.remote.PlanetsPagingSource
import com.br.myapplication.data.repository.planet.IPlanetRepository
import kotlinx.coroutines.launch

class PlanetsViewModel(
    val repository: IPlanetRepository,
    val planetsDao: PlanetsDao
) : ViewModel() {

    private val _planetList = Pager(
        config = PagingConfig(pageSize = 10),
        initialKey = 1
    ) {
        PlanetsPagingSource(repository, planetsDao)
    }.flow.cachedIn(viewModelScope).asLiveData()

    val planetList: LiveData<PagingData<Planet>>
        get() = _planetList

    private val _filterText = MutableLiveData<String>()

    val filteredPlanetList: LiveData<PagingData<Planet>> = _filterText.switchMap { filter ->
        if (filter.isNullOrBlank()) {
            _planetList
        } else {
            getFilteredPlanetList(filter)
        }
    }

    fun setFilter(filter: String) {
        _filterText.value = filter
    }

    private fun getFilteredPlanetList(filter: String): LiveData<PagingData<Planet>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { planetsDao.getFilteredPlanetsPagingSource(filter) }
        ).flow.cachedIn(viewModelScope).asLiveData()
    }

    fun updatePlanet(planet: Planet) {
        viewModelScope.launch {

            planetsDao.updatePlanet(planet)
        }
    }
}