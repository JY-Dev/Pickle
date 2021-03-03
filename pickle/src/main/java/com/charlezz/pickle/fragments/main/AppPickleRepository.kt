package com.charlezz.pickle.fragments.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.charlezz.pickle.data.entity.Media
import com.charlezz.pickle.util.PickleConstants
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import timber.log.Timber


class AppPickleRepository constructor(
    private val context: Context
) : PickleRepository {

    private var currentPagingSource: PicklePagingSource? = null

    private val count = MutableLiveData<Int>()

    init {
        Timber.d("AppPickleRepository")
    }
    override fun getItems(
        selectionType: PicklePagingSource.SelectionType,
        bucketId: Long?,
        startPosition: Int,
        pageSize: Int
    ): Flow<PagingData<Media>>{
        Timber.d("getItems# selectionType = $selectionType, bucketId = $bucketId, startPosition = $startPosition, pageSize = $pageSize")
        return Pager(
            PagingConfig(
                pageSize = pageSize,
                enablePlaceholders = true,
                initialLoadSize = PickleConstants.DEFAULT_PAGE_SIZE
            ),
            initialKey = startPosition,
        ) {
            PicklePagingSource(
                context = context,
                bucketId = bucketId,
                selectionType = selectionType,
                count = count
            ).also {
                currentPagingSource = it
            }
        }.flow
    }
    override fun invalidate(){
        currentPagingSource?.invalidate()
    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun getCount(): LiveData<Int> = count

}