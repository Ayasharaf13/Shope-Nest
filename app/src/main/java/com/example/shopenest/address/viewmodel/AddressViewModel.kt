package com.example.shopenest.address.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shopenest.model.*

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response


class AddressViewModel  (private val repo: RepositoryInterface) : ViewModel(){


//CustomerAddressResponse


    private val _updateCustomer =MutableSharedFlow <Response<CustomerResponse>>()
    val  updateCustomer = _updateCustomer.asSharedFlow()
    // Expose as a read-only StateFlo
   // val updateCustomer: StateFlow<Response<CustomerResponse>?> get() = _updateCustomer




    private val _createCustomerAddress = MutableSharedFlow<CustomerAddressResponse>()
    val createCustomerAddress = _createCustomerAddress.asSharedFlow()


    //   private val _createCustomerAddress = MutableStateFlow <CustomerAddressResponse?>(null)
    // Expose as a read-only StateFlo
  //  val createCustomerAddress: StateFlow<CustomerAddressResponse?> get() = _createCustomerAddress

   // CustomerAddressesResponse

   // private val _addressesForCustomer = MutableStateFlow< List<CustomerAddressesResponse?>>(null)
    // Expose as a read-only StateFlo
   // val addressesForCustomer: StateFlow<List<CustomerAddressesResponse?>> get() = _addressesForCustomer





     private val _address = MutableStateFlow<List<CustomerAddress>?>(null)
       // Expose as a read-only StateFlo
       val address: StateFlow<List<CustomerAddress>?> get() = _address



       private val _addressID = MutableStateFlow<CustomerAddress?>(null)
       // Expose as a read-only StateFlo
       val addressID: StateFlow<CustomerAddress?> get() = _addressID


     //  private val _defaultAddress = MutableStateFlow<CustomerAddress?>(null)
     //  val defaultAddress: StateFlow<CustomerAddress?> get() = _defaultAddress




    init {

        getAllAddress()
    }


    fun getAddresses (customerId:Long){

        viewModelScope.launch  (Dispatchers.IO){

            try{
                val res = repo.getCustomerAddresses(customerId)
                _address.value = res.addresses
            }catch (e:HttpException){
                Log.i ("GetAddressesForCustomer","HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}")
            }
        }

    }


    fun updateCustomer (customerId:Long,body: CustomerRequest){

        viewModelScope.launch  (Dispatchers.IO){
            try {
                val response = repo.updateCustomer(customerId,body)

                // ✅ Update StateFlow with result
                _updateCustomer.emit(response)

               val name = response.body()?.customer?.first_name

                Log.i("name::customerupdate",name.toString())

            } catch (e: HttpException) {
                Log.e(
                    "updateCustomer",
                    "HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}"
                )
              //  _updateCustomer.value = null
            } catch (e: Exception) {
                Log.e("updateCustomer", "Unexpected error: ${e.message}")
               // _updateCustomer.value = null
            }
        }

    }



    fun createCustomerAddress(customerId:Long , body:CreateCustomerAddressRequest){


        viewModelScope.launch  (Dispatchers.IO){
            try {
                val response = repo.createCustomerAddress(customerId,body)

                // ✅ Update StateFlow with result
                val name = response.customer_address.first_name
                val phone= response.customer_address.phone
                _createCustomerAddress.emit( response)
                Log.e("createCustomerAddressCustomer", "Success: $name $phone ")
            } catch (e: HttpException) {
                Log.e(
                    "createCustomerAddressCustomer",
                    "HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}"
                )

            } catch (e: Exception) {
                Log.e("createCustomerAddressCustomer", "Unexpected error: ${e.message}")

            }
        }




    }


    fun getAllAddress (){
       /* viewModelScope.launch  (Dispatchers.IO){

            try{
                val res = repo.getAllAddresses()
                 res.collect{
                     _address.value = it
                }
            }catch (e: HttpException){
                Log.i ("addressCustmer","HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}")
            }
        }

        */
    }



    fun getAddressById (id:Int){
       /* viewModelScope.launch  (Dispatchers.IO){

            try{
                val res = repo.getAddressByIdOnce(id)

                _addressID.value = res
            }catch (e: HttpException){
                Log.i ("addressCustmerByID","HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}")
            }
        }

        */
    }



  /*  fun saveAddress (address:CustomerAddress) {

        viewModelScope.launch(Dispatchers.IO) {

            try {
                repo.saveAddress(address)

            } catch (e: Exception) {
                Log.i("ErrorSaving", e.message.toString())

            }
        }


    }

   */


    fun deleteAddress (address:CustomerAddress) {

      /*  viewModelScope.launch(Dispatchers.IO) {

            try {
                repo.deleteAddress(address)

            } catch (e: Exception) {
                Log.i("ErrorDelete", e.message.toString())

            }
        }

       */


    }


    fun getDefaultAddress() {
      /*  viewModelScope.launch(Dispatchers.IO) {
            try {
                val address = repo.getDefaultAddress()
                _defaultAddress.value = address
            } catch (e: Exception) {
                Log.e("AddressViewModel", "Error fetching default address: ${e.message}")
            }
        }

       */

    }

    fun setDefaultAddress(customerId: Long , addressId:Long) {
        viewModelScope.launch(Dispatchers.IO) {

            try {

                val response = repo.setDefaultAddress(customerId, addressId)

                // ✅ Success: update StateFlow
                _address.value = listOf(response.customer_address)

                Log.d("DefaultAddress", "Default set to: ${response.customer_address.address1}")

            } catch (e: HttpException) {
                Log.e("DefaultAddress", "HTTP ${e.code()} - ${e.response()?.errorBody()?.string()}")

            } catch (e: Exception) {
                Log.e("DefaultAddress", "Unexpected: ${e.message}")

            }


        }


    }





}