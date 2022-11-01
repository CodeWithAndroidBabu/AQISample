package co.android.helper.hkaqi.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class NetworkCallBack : CoroutineDispatcher(){
    suspend fun executeNetworkCall() = withContext(Dispatchers.IO){

    }

}