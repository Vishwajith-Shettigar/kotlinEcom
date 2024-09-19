package com.example.ecomapp.Viewmodel

import android.graphics.pdf.PdfDocument.PageInfo
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.ecomapp.Data.Product
import com.example.ecomapp.Util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryviewmodel @Inject constructor(

  private val firestore: FirebaseFirestore,

  ) : ViewModel() {

  private val paginginfo = Paginginfo()

  private val _speicalproducts =
    MutableStateFlow<com.example.ecomapp.Util.Resource<List<Product>>>(Resource.Notspecified())

  public val specialproduts: StateFlow<Resource<List<Product>>> = _speicalproducts

  private val _bestdealproducts =
    MutableStateFlow<com.example.ecomapp.Util.Resource<List<Product>>>(Resource.Notspecified())

  public val bestdealproducts: StateFlow<Resource<List<Product>>> = _bestdealproducts


  private val _bestproduct =
    MutableStateFlow<com.example.ecomapp.Util.Resource<List<Product>>>(Resource.Notspecified())

  public val bestproducts: StateFlow<Resource<List<Product>>> = _bestproduct

  init {
    fetchSpecialProducts()
  }

  fun fetchSpecialProducts() {
    viewModelScope.launch {

      _speicalproducts.emit(Resource.Loading())
    }

    firestore.collection("Products").whereEqualTo("category", "special product")
      .get().addOnSuccessListener { result ->
        var specialproducstlist = result.toObjects(Product::class.java)


        viewModelScope.launch {

          _speicalproducts.emit(Resource.Success(specialproducstlist))
        }
      }

      .addOnFailureListener {

        viewModelScope.launch {

          _speicalproducts.emit(Resource.Error(it.message.toString()))
        }
      }
  }


  fun fetchBestdealsproducts() {
    viewModelScope.launch {

      _bestdealproducts.emit(Resource.Loading())
    }

    firestore.collection("Products").whereEqualTo("category", "best deal product")
      .get().addOnSuccessListener { result ->
        var specialproducstlist = result.toObjects(Product::class.java)


        viewModelScope.launch {

          _bestdealproducts.emit(Resource.Success(specialproducstlist))
        }
      }

      .addOnFailureListener {

        viewModelScope.launch {

          _bestdealproducts.emit(Resource.Error(it.message.toString()))
        }
      }
  }


  fun fetchbestproducts() {
    if (!paginginfo.isPagingend) { // if allproducts all fetched so not more to fetch just to
      // make fire base query read more efficient
      viewModelScope.launch {

        _bestproduct.emit(Resource.Loading())
      }

      firestore.collection("Products")
        .limit(paginginfo.page * 10).get().addOnSuccessListener { result ->
          var specialproducstlist = result.toObjects(Product::class.java)
          paginginfo.isPagingend = specialproducstlist == paginginfo.oldpagesproducts
          paginginfo.oldpagesproducts = specialproducstlist



          viewModelScope.launch {

            _bestproduct.emit(Resource.Success(specialproducstlist))
          }
          paginginfo.page++
        }

        .addOnFailureListener {

          viewModelScope.launch {

            _bestproduct.emit(Resource.Error(it.message.toString()))
          }
        }
    } else {
      Unit
    }

  }

  internal data class Paginginfo(
    public var page: Long = 1,
    public var oldpagesproducts: List<Product> = emptyList(),

    var isPagingend: Boolean = false,

    ) {

  }
}


