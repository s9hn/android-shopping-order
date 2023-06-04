package woowacourse.shopping.data.datasource.remote.product

import android.util.Log
import com.example.domain.util.CustomResult
import woowacourse.shopping.data.remote.ServiceFactory
import woowacourse.shopping.data.remote.response.ProductResponseDto
import java.util.concurrent.Executors

class ProductDataSourceImpl : ProductDataSource {

    override fun getSubListProducts(
        limit: Int,
        scrollCount: Int,
    ): Result<List<ProductResponseDto>> {
        val executor = Executors.newSingleThreadExecutor()
        val result = executor.submit<Result<List<ProductResponseDto>>> {
            val response = ServiceFactory.shoppingService.getProducts(limit, scrollCount).execute()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Throwable(response.message()))
            }
        }.get()
        executor.shutdown()
        return result
    }

    override fun getProducts(
        limit: Int,
        scrollCount: Int,
        onSuccess: (CustomResult<List<ProductResponseDto>>) -> Unit,
        onFailure: (CustomResult<Error>) -> Unit,
    ) {
        Log.d("!23", "123")
    }
}
