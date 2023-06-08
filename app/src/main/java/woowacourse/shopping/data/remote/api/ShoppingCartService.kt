package woowacourse.shopping.data.remote.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import woowacourse.shopping.data.remote.request.CartItemRequest
import woowacourse.shopping.data.remote.response.CartProductDto

interface ShoppingCartService {
    @GET("/cart-items")
    fun getAllProductInCart(): Call<List<CartProductDto>>

    @POST("/cart-items")
    fun postProductToCart(
        @Body cartItemRequest: CartItemRequest,
    ): Call<Void>

    @PATCH("/cart-items/{cartItemId}")
    fun patchProductCount(
        @Path("cartItemId") cartItemId: Long,
        @Body quantity: Int,
    ): Call<Unit>

    @DELETE("/cart-items/{cartItemId}")
    fun deleteProductInCart(
        @Path("cartItemId") cartItemId: Long,
    ): Call<Unit>
}
