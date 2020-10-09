package com.joaovitor.tucaprodutosdelimpeza.data

import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.joaovitor.tucaprodutosdelimpeza.data.model.ProductSale
import com.joaovitor.tucaprodutosdelimpeza.data.model.StockMovement
import com.joaovitor.tucaprodutosdelimpeza.data.util.Firestore
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class StockRepository {

    suspend fun getStockMovements(productId: String): Result<List<StockMovement>> {
        return try {
            val stockMovements = FirebaseFirestore
                .getInstance()
                .collection(Firestore.COL_PRODUCTS)
                .document(productId)
                .collection(Firestore.SUBCOL_STOCK_MOVEMENT)
                .orderBy(Firestore.STOCK_MOVEMENT_DATE, Query.Direction.DESCENDING)
                .get().await()
            val list = stockMovements.map { it.toObject(StockMovement::class.java) }

            Result.Success(list)
        }catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun addStockMovement(products: List<ProductSale>, saleId: Int): Result<Any> {
          return try {
              for (product in products) {
                  val stockMovement = StockMovement(
                      quantity = product.quantity,saleId = saleId,isStockChange = false )

                  /* Register the stock movement */
                  val productDocRef = FirebaseFirestore
                      .getInstance()
                      .collection(Firestore.COL_PRODUCTS)
                      .document(product.parentId)
                  productDocRef.collection(Firestore.SUBCOL_STOCK_MOVEMENT).add(stockMovement)
                      .addOnFailureListener {
                          FirebaseCrashlytics.getInstance().recordException(it)
                      }

                  /* Update the current stock */
                  val currentStock = (productDocRef.get().await().get("currentStock") as Long).toInt()
                  productDocRef.update(Firestore.PRODUCT_CURRENT_STOCK, currentStock - product.quantity)
                      .addOnFailureListener {
                          FirebaseCrashlytics.getInstance().recordException(it)
                      }
              }

              Result.Success(null)
          } catch (e: Exception) {
              Result.Error(e)
          }
    }

    suspend fun addStockChange(productId: String, user: String, newStock: Int): Result<Any> {
        return try {
            val calculateResult = StockRepository().recalculateStock(productId)

            if(calculateResult is Result.Error) {
                throw Exception("Error on recalculateStock()")
            }

            val stockMovement = StockMovement(
                isStockChange = true,
                quantity = newStock - (calculateResult as Result.Success).data!!,
                seller = user,
                newStock = newStock)

            val productDocRef = FirebaseFirestore
                .getInstance()
                .collection(Firestore.COL_PRODUCTS)
                .document(productId)

            productDocRef.collection(Firestore.SUBCOL_STOCK_MOVEMENT).add(stockMovement)

            productDocRef.update(Firestore.PRODUCT_CURRENT_STOCK, newStock)

            Result.Success(null)
        }catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun deleteStockMovement(saleId: Int): Result<Any> {
        return try {
            val productsRef = ProductRepository().getProductsRefs()

            if(productsRef is Result.Error) {
                throw Exception(productsRef.exception)
            }

            for (productRef in (productsRef as Result.Success).data!!) {
                val stockMovements = productRef.reference
                    .collection(Firestore.SUBCOL_STOCK_MOVEMENT).get().await()

                for (stockMovement in stockMovements) {
                    val mStockHistory = stockMovement.toObject(StockMovement::class.java)
                    if(!mStockHistory.isStockChange!!
                                && mStockHistory.saleId == saleId) {
                        stockMovement.reference.delete().await()
                    }
                }

                recalculateStock(productId = productRef.id)
            }

            Result.Success(null)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun recalculateStock(productId: String): Result<Int> {
        return try {
            val resultStockMovements = getStockMovements(productId)

            if(resultStockMovements is Result.Error) {
                throw Exception(resultStockMovements.exception)
            }

            val stockMovements = (resultStockMovements as Result.Success).data!!
            val mStockMovements: MutableList<StockMovement> = mutableListOf()
            var lastStockAdded = 0

            loop@ for(stockMovement in stockMovements) {
                if(stockMovement.isStockChange!!){
                    lastStockAdded =  stockMovement.newStock?:
                            throw Exception("Stock change without property new stock")
                    break@loop
                } else {
                    mStockMovements.add(stockMovement)
                }
            }

            var stockQuantityUsed = 0

            if(mStockMovements.size > 0) {
                stockQuantityUsed = mStockMovements.map {it.quantity}.reduce {acc, quantity -> acc + quantity}
            }

            val currentStock =  lastStockAdded - stockQuantityUsed

            FirebaseFirestore.getInstance()
                .collection(Firestore.COL_PRODUCTS)
                .document(productId)
                .update(Firestore.PRODUCT_CURRENT_STOCK, currentStock)
                .await()

            Result.Success(currentStock)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

}