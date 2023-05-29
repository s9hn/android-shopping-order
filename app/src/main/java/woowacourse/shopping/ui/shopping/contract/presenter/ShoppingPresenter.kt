package woowacourse.shopping.ui.shopping.contract.presenter

import com.example.domain.repository.CartRepository
import com.example.domain.repository.ProductRepository
import com.example.domain.repository.RecentRepository
import woowacourse.shopping.mapper.toUIModel
import woowacourse.shopping.ui.shopping.ProductItem
import woowacourse.shopping.ui.shopping.ProductReadMore
import woowacourse.shopping.ui.shopping.ProductsItemType
import woowacourse.shopping.ui.shopping.RecentProductsItem
import woowacourse.shopping.ui.shopping.contract.ShoppingContract

class ShoppingPresenter(
    private val view: ShoppingContract.View,
    private val productRepository: ProductRepository,
    private val recentRepository: RecentRepository,
    private val cartRepository: CartRepository,
    offset: Int = 0,
) : ShoppingContract.Presenter {
    private var productsData: MutableList<ProductsItemType> = mutableListOf()
    private var productSize: Int = 0
    private var productOffset = ProductsOffset(offset, productRepository)
    override fun initProducts() {
        productRepository.getMoreProducts(
            productOffset.getOffset(),
            PRODUCT_COUNT,
            onSuccess = { products ->
                productSize += products.size
                productsData += products.map {
                    ProductItem(it.toUIModel(), getCount(it.id))
                }
                view.setProducts(productsData.plus(ProductReadMore))
            },
        )
    }

    override fun updateProducts() {
        val recentProductsData = RecentProductsItem(
            recentRepository.getRecent(RECENT_PRODUCT_COUNT).map { it.toUIModel() },
        )
        when {
            productsData.isEmpty() && recentProductsData.product.isNotEmpty() -> productsData.add(
                recentProductsData,
            )

            productsData.isNotEmpty() -> {
                updateProductsDataWithRecentData(recentProductsData)
            }
        }
        view.setProducts(productsData.plus(ProductReadMore))
    }

    private fun updateProductsDataWithRecentData(recentProductsData: RecentProductsItem) {
        when {
            recentProductsData.product.isEmpty() -> productsData.removeIf { it is RecentProductsItem }
            productsData[0] is RecentProductsItem -> productsData[0] = recentProductsData
            else -> productsData.add(0, recentProductsData)
        }
    }

    override fun fetchMoreProducts() {
        productOffset = productOffset.plus(PRODUCT_COUNT)
        productRepository.getMoreProducts(
            productOffset.getOffset(),
            PRODUCT_COUNT,
            onSuccess = { products ->
                productSize += products.size
                productsData += products.map {
                    ProductItem(it.toUIModel(), getCount(it.id))
                }
                view.addProducts(productsData.plus(ProductReadMore))
            },
        )
    }

    override fun navigateToItemDetail(id: Long) {
        val latestProduct = recentRepository.getRecent(1).firstOrNull()?.toUIModel()
        view.navigateToProductDetail(id, latestProduct)
    }

    override fun updateItemCount(id: Long, count: Int) {
//        productRepository.findById(id, onSuccess = {
//            cartRepository.insert(CartProduct(it, count, true))
//        }, onFailure = {
//            // Handle failure case
//        })
//        updateCountSize()
    }

    override fun increaseCount(id: Long) {
//        cartRepository.updateCount(id, getCount(id) + 1)
        view.updateItem(id, getCount(id))
    }

    override fun decreaseCount(id: Long) {
//        cartRepository.updateCount(id, getCount(id) - 1)
        view.updateItem(id, getCount(id))
    }

    override fun updateCountSize() {
//        view.showCountSize(cartRepository.getAll().size)
    }

    private fun getCount(id: Long): Int {
//        cartRepository.findById(id)?.let {
//            return it.count
//        }
        return 0
    }

    companion object {
        private const val RECENT_PRODUCT_COUNT = 10
        private const val PRODUCT_COUNT = 20
    }
}
