package com.example.nenmott
//
//import android.content.Context
//import android.net.ConnectivityManager
//import android.net.Network
//import android.net.NetworkCapabilities
//import android.net.NetworkRequest
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//
//
//@Composable
//fun InternetStatusIndicator() {
//    val context = LocalContext.current
//    val isConnected by getNetworkStatus(context).observeAsState(true)
//
//    if (!isConnected) {
//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.Red)
//                .padding(8.dp),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(text = "No Internet Connection", color = Color.White)
//        }
//    }
//}
//
//fun getNetworkStatus(context: Context): LiveData<Boolean> {
//    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//    val networkStatus = MutableLiveData<Boolean>()
//
//    val networkCallback = object : ConnectivityManager.NetworkCallback() {
//        override fun onAvailable(network: Network) {
//            networkStatus.postValue(true)
//        }
//
//        override fun onLost(network: Network) {
//            networkStatus.postValue(false)
//        }
//    }
//
//    val networkRequest = NetworkRequest.Builder()
//        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
//        .build()
//
//    connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
//
//    return networkStatus
//}
