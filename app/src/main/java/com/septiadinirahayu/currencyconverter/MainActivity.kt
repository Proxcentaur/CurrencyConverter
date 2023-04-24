package com.septiadinirahayu.currencyconverter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.septiadinirahayu.currencyconverter.bean.Currency
import com.septiadinirahayu.currencyconverter.bean.CurrencyRateResponse
import com.septiadinirahayu.currencyconverter.bean.ErrorBean
import com.septiadinirahayu.currencyconverter.bean.model.CurrencyConversionViewModelFactory
import com.septiadinirahayu.currencyconverter.databinding.ActivityMainBinding
import com.septiadinirahayu.currencyconverter.databinding.CurrencyDetailViewBinding
import com.septiadinirahayu.currencyconverter.databinding.RvItemViewHeaderBinding
import com.septiadinirahayu.currencyconverter.databinding.RvItemViewListBinding
import com.septiadinirahayu.currencyconverter.domain.NetworkDomain
import com.septiadinirahayu.currencyconverter.viewmodel.CurrencyConversionViewModel
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<CurrencyConversionViewModel> {
        CurrencyConversionViewModelFactory(NetworkDomain())
    }

    private var dataBinding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_CurrencyConverter)
        super.onCreate(savedInstanceState)
        App.applicationContext = applicationContext
        setView()
        viewModel.getCurrencyRate()
        initObserver()
    }

    private fun setView() {
        dataBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(dataBinding?.root)
    }

    private fun setRecyclerView(successResponse: CurrencyRateResponse?) {
        val currencyConversionAdapter = CurrencyConversionAdapter()
        dataBinding?.recyclerView?.apply {
            layoutManager = LinearLayoutManager(this@MainActivity, LinearLayoutManager.VERTICAL, false)
            isNestedScrollingEnabled = true
            setHasFixedSize(true)
            adapter = currencyConversionAdapter
        }

        val dataSet = ArrayList<RecyclerViewItem>()
        dataSet.add(HeaderItem(successResponse))
        dataSet.add(CurrencyListItem(successResponse?.getListConversion()))
        currencyConversionAdapter.data = dataSet

        currencyConversionAdapter.notifyDataSetChanged()
    }
    override fun onDestroy() {
        super.onDestroy()
        dataBinding = null
    }

    private fun initObserver() {
        viewModel.uiState.observe(this) { conversionUiState ->
            val isError = conversionUiState?.errorResponse?.error == true
            if (!isError) {
                Log.w("dinnn", "initObserver: ${conversionUiState.successResponse?.rates}", )
                setOnSuccessView(conversionUiState.successResponse)
            } else {
                setOnErrorView(conversionUiState.errorResponse)
            }
        }
    }

    private fun setOnSuccessView(successResponse: CurrencyRateResponse?) {
        setRecyclerView(successResponse)
    }

    private fun setOnErrorView(errorResponse: ErrorBean?) {
        val errorMessage = errorResponse?.description ?: "An error has occurred"
        dataBinding?.getRoot()?.let {
            showSnackBar(it, errorMessage)
        }
    }

}

open class RecyclerViewItem

class HeaderItem(val successResponse: CurrencyRateResponse?) : RecyclerViewItem()

class CurrencyListItem(val listConversion:  List<Pair<Currency, Double?>>?) : RecyclerViewItem()

class CurrencyConversionAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var data = listOf<RecyclerViewItem>()
    private var rv: RecyclerView? = null

    companion object {
        private const val VIEW_TYPE_HEADER = 1
        private const val VIEW_TYPE_ITEM_LIST = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (data[position] is HeaderItem) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM_LIST
        }
    }

    class HeaderViewHolder(val dataBinding: RvItemViewHeaderBinding, val rv: RecyclerView?) : RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(item: HeaderItem) {
            val listCurrency = item.successResponse?.getListCurrencyName()
            val listRates = item.successResponse?.getListRates()
            listCurrency?.let {
                val spRatesAdapter: ArrayAdapter<String> = ArrayAdapter(dataBinding.root.context, android.R.layout.simple_spinner_item, it)
                spRatesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                dataBinding.spinnerCurrency.adapter = spRatesAdapter
            }

            dataBinding.spinnerCurrency.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
                {
                    val amount = dataBinding.editAmount.editText?.text.toString()
                    var result = 0.0
                    val selectedRatesValue: Double = listRates?.get(position) ?: 0.0
                    if (amount.isNotEmpty()) {
                        result = amount.toDouble() * (selectedRatesValue)

                        val linearContents = rv?.getChildAt(1)?.findViewById<LinearLayout>(R.id.linear_contents)
                        linearContents?.removeAllViews()

                        item.successResponse?.getListConversion()?.forEach {
                            val resultInList = amount.toDouble() * (it.second ?: 0.0)

                            val itemView = CurrencyDetailViewBinding.inflate(LayoutInflater.from(dataBinding.root.context), linearContents, true)
                            itemView.tvCurrency.text = it.first.toString()
                            itemView.tvRates.text = getFormattedRates(resultInList)

                        }
                    }
                    dataBinding.editAmount.editText?.setText(getFormattedRates(result))

                }
                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }

        }
    }

    class CurrencyListViewHolder(private val dataBinding: RvItemViewListBinding) : RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(item: CurrencyListItem) {
            val linearContents = dataBinding.linearContents
            linearContents.removeAllViews()

            item.listConversion?.forEachIndexed { index, pair ->
                val isLastItem = index == item.listConversion.lastIndex

                val itemView = CurrencyDetailViewBinding.inflate(LayoutInflater.from(dataBinding.root.context), linearContents, true)
                itemView.tvCurrency.text = pair.first.toString()
                itemView.tvRates.text = getFormattedRates(pair.second ?: 0.0)

                if (isLastItem) {
                    itemView.line.visibility = View.GONE
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val dataBinding = RvItemViewHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                HeaderViewHolder(dataBinding, rv)
            }
            VIEW_TYPE_ITEM_LIST -> {
                val dataBinding = RvItemViewListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CurrencyListViewHolder(dataBinding)
            }
            else -> throw java.lang.IllegalArgumentException ("Invalid item view type")
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data[position]
        if (holder is HeaderViewHolder && item is HeaderItem) {
            holder.bind(item)
        }
        if (holder is CurrencyListViewHolder && item is CurrencyListItem) {
            holder.bind(item)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        rv = recyclerView
    }
}



