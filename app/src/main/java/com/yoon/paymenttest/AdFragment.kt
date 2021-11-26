package com.yoon.paymenttest

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.Purchase
import com.android.billingclient.api.SkuDetails
import com.yoon.paymenttest.databinding.FragmentAdBinding

class AdFragment : BaseFragment<FragmentAdBinding>() {

    private lateinit var bm: BillingModule

    override fun getViewBinding(): FragmentAdBinding {
        return FragmentAdBinding.inflate(layoutInflater)
    }

    private var mSkuDetails = listOf<SkuDetails>()
        set(value) {
            field = value
            setSkuDetailsView()
        }

    // 이전에 광고 제거 구매 여부
    private var isPurchasedRemoveAds = false
        set(value) {
            field = value
            updateRemoveAdsView()
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bm = BillingModule(activity!!, lifecycleScope, object: BillingModule.Callback {
            override fun onBillingModulesIsReady() {
                bm.querySkuDetail(BillingClient.SkuType.INAPP, Sku.REMOVE_ADS, Sku.BUY_1000) { skuDetails ->
                    mSkuDetails = skuDetails
                }

                bm.checkPurchased(Sku.REMOVE_ADS) {
                    isPurchasedRemoveAds = it
                }
            }

            override fun onSuccess(purchase: Purchase) {
                when (purchase.sku) {
                    Sku.REMOVE_ADS -> {
                        isPurchasedRemoveAds = true
                    }
                    Sku.BUY_1000 -> {
                        // 크리스탈 1000개를 충전합니다.
                        val currentCrystal = SavedData.crystal
                        SavedData.crystal =  currentCrystal + 1000
                        updateCrystalView()
                    }
                }
            }

            override fun onFailure(errorCode: Int) {
                when (errorCode) {
                    BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED -> {
                        Toast.makeText(context, "이미 구입한 상품입니다.", Toast.LENGTH_LONG).show()
                    }
                    BillingClient.BillingResponseCode.USER_CANCELED -> {
                        Toast.makeText(context, "구매를 취소하셨습니다.", Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(context, "error: $errorCode", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateCrystalView()
        setClickListeners()
    }


    private fun setClickListeners() {
        with (mBinding) {
            // 광고 제거 구매 버튼 클릭
            btnPurchaseRemoveAds.setOnClickListener {
                mSkuDetails.find { it.sku == Sku.REMOVE_ADS }?.let { skuDetail ->
                    bm.purchase(skuDetail)
                } ?: also {
                    Toast.makeText(context, "상품을 찾을 수 없습니다.", Toast.LENGTH_LONG).show()
                }
            }

            btnPurchaseCrystal.setOnClickListener {
                mSkuDetails.find { it.sku == Sku.BUY_1000 }?.let { skuDetail ->
                    bm.purchase(skuDetail)
                } ?: also {
                    Toast.makeText(context, "상품을 찾을 수 없습니다.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun setSkuDetailsView() {
        val builder = StringBuilder()
        for (skuDetail in mSkuDetails) {
            builder.append("<${skuDetail.title}>\n")
            builder.append(skuDetail.price)
            builder.append("\n======================\n\n")
        }
        mBinding.tvSku.text = builder
    }

    private fun updateRemoveAdsView() {
        mBinding.tvRemoveAds.text = "광고 제거 여부: ${if (isPurchasedRemoveAds) "O" else "X"}"
    }

    private fun updateCrystalView() {
        mBinding.tvCrystal.text = "보유 크리스탈: ${SavedData.crystal}"
    }

    override fun onResume() {
        super.onResume()
        bm.onResume(BillingClient.SkuType.INAPP)
    }

}