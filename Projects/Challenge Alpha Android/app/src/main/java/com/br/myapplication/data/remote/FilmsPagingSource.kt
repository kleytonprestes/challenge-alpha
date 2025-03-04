package com.br.myapplication.data.remote

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.br.myapplication.data.dao.FilmsDao
import com.br.myapplication.data.model.Film
import com.br.myapplication.data.repository.film.IFilmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FilmsPagingSource(
    private val filmRepository: IFilmRepository,
    private val filmsDao: FilmsDao,
) : PagingSource<Int, Film>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
        return try {

            val page = params.key ?: 1

            withContext(Dispatchers.IO) {
                val filmsFromApi = filmRepository.fetchFilmList(page.toString())

                filmsFromApi.forEach { filmsDao.insertFilm(it) }

                val filmsFromDb = filmsDao.getAllFilmsPaging((page - 1) * params.loadSize, params.loadSize)

                LoadResult.Page(
                    data = filmsFromDb,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = page + 1
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Film>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}