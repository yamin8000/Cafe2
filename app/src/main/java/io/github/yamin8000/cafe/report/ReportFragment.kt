package io.github.yamin8000.cafe.report

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import io.github.yamin8000.cafe.R
import io.github.yamin8000.cafe.databinding.FragmentReportBinding
import io.github.yamin8000.cafe.db.entities.order.OrderStatus
import io.github.yamin8000.cafe.db.entities.relatives.ProductAndCategory
import io.github.yamin8000.cafe.order.searchorder.SearchOrderAdapter
import io.github.yamin8000.cafe.product.ProductsAdapter
import io.github.yamin8000.cafe.ui.recyclerview.EmptyAdapter
import io.github.yamin8000.cafe.ui.util.BaseFragment
import io.github.yamin8000.cafe.util.Constants.CATEGORY
import io.github.yamin8000.cafe.util.Constants.FILTER
import io.github.yamin8000.cafe.util.Constants.NAME
import io.github.yamin8000.cafe.util.Constants.PRICE
import io.github.yamin8000.cafe.util.Constants.db
import io.github.yamin8000.cafe.util.Utility.handleCrash
import io.github.yamin8000.cafe.util.Utility.navigate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File


class ReportFragment : BaseFragment<FragmentReportBinding>({ FragmentReportBinding.inflate(it) }) {

    private var reports = listOf<String>()

    private val emptyAdapter by lazy(LazyThreadSafetyMode.NONE) { EmptyAdapter() }

    private val ioScope by lazy(LazyThreadSafetyMode.NONE) { CoroutineScope(Dispatchers.IO) }

    private var items = listOf<Any>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            binding.reportList.adapter = emptyAdapter
            fillReportList()
            fillReportsAutoComplete()
            reportsAutoCompleteClickListener()
            shareCsvHandler()
        } catch (e: Exception) {
            handleCrash(e)
        }
    }

    private fun shareCsvHandler() {
        binding.shareCsvButton.setOnClickListener {
            context?.let {
                val file = File.createTempFile("report", ".csv", it.cacheDir)
                csvWriter().writeAll(listOf(items), file.outputStream())
                csvWriter().open(file.outputStream()) {
                    writeRow(listOf("Name", "Price", "Category"))
                    items.forEach { item ->
                        if (item is ProductAndCategory) {
                            writeRow(
                                item.product?.name ?: "",
                                item.product?.price ?: "",
                                item.category.name
                            )
                        }
                    }
                }

                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, file.readText())
                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)

            }

        }
    }

    private fun reportsAutoCompleteClickListener() {
        binding.reportTypeEdit.setOnItemClickListener { parent, _, position, _ ->
            val report = parent.getItemAtPosition(position) as String
            handleReport(report)
        }
    }

    private fun handleReport(report: String) {
        when (report) {
            getString(R.string.orders) -> lifecycleScope.launch { test() }
            getString(R.string.products) -> lifecycleScope.launch { handleProductsReport() }
        }
    }

    private fun handleProductsReport() {
        navigate(R.id.productFilterModal)
        setFragmentResultListener(FILTER) { _, bundle ->
            val name = bundle.getString(NAME)
            val category = bundle.getString(CATEGORY)
            val price = bundle.getString(PRICE)
            lifecycleScope.launch { handleFilterArguments(name, category, price) }
        }
    }

    private suspend fun handleFilterArguments(
        name: String?,
        category: String?,
        price: String?
    ) {
        val products = withContext(ioScope.coroutineContext) {
            Reporters.productReports(name, category, price)
        }
        items = products

        val adapter = ProductsAdapter({}, { _, _ -> })
        adapter.asyncList.submitList(products)
        binding.reportList.adapter = adapter
    }

    private fun fillReportList() {
        context?.let {
            reports = it.resources.getStringArray(R.array.report_values).toList()
        }
    }

    private fun fillReportsAutoComplete() {
        context?.let {
            ArrayAdapter(it, R.layout.dropdown_item, reports).let { adapter ->
                binding.reportTypeEdit.setAdapter(adapter)
            }
        }
    }

    private suspend fun test() {
        val orders = db?.relativeDao()?.getOrderWithDetails() ?: listOf()
        val reportOrders = orders.asSequence()
            .filter { it.orderAndSubscriber.order.status == OrderStatus.Registered }
            .filter { it.orderAndSubscriber.order.totalPrice < 5 }
            .toList()

        val adapter = SearchOrderAdapter()
        adapter.asyncList.submitList(reportOrders)
        binding.reportList.adapter = adapter
    }
}