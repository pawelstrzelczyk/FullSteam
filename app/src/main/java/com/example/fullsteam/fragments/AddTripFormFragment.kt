package com.example.fullsteam.fragments

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.fullsteam.R
import com.example.fullsteam.components.BrandSpinnerAdapter
import com.example.fullsteam.components.CurrencySpinnerAdapter
import com.example.fullsteam.koleo.KoleoClient
import com.example.fullsteam.koleo.brands.Brand

class AddTripFormFragment : Fragment() {
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var tripDateEditText: EditText
    private lateinit var brandSpinner: Spinner
    private lateinit var trainNumberEditText: EditText
    private var brandsList: ArrayList<Brand> = arrayListOf()
    private val currencyList: ArrayList<String> = arrayListOf("PLN", "EUR")
    private lateinit var currencySpinner: Spinner
    private lateinit var koleoClient: KoleoClient

    override fun onCreate(savedInstanceState: Bundle?) {
        koleoClient = KoleoClient()

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_add_trip_form, container, false)
        var brandAdapter = BrandSpinnerAdapter(
            requireContext(),
            brandsList.sortedBy { it.name })
        val currencyAdapter: ArrayAdapter<String> =
            CurrencySpinnerAdapter(requireContext(), currencyList)

        koleoClient.getBrands().observeForever {

            (0 until it[0].brands.size).forEach { i ->
                brandsList.add(it[0].brands[i])
            }
            brandAdapter.notifyDataSetChanged()

        }
        brandAdapter = BrandSpinnerAdapter(
            requireContext(),
            brandsList
        )

        tripDateEditText = fragmentView.findViewById(R.id.trip_date_edit_text)
        brandSpinner = fragmentView.findViewById(R.id.train_brand_spinner)
        currencySpinner = fragmentView.findViewById(R.id.trip_currency_option)
        brandSpinner.adapter = brandAdapter
        currencySpinner.adapter = currencyAdapter

        tripDateEditText.inputType = InputType.TYPE_NULL
        trainNumberEditText = fragmentView.findViewById(R.id.train_number_edit_text)
        tripDateEditText.setOnClickListener {

            val calendar: Calendar = Calendar.getInstance();
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)

            datePickerDialog = DatePickerDialog(
                fragmentView.context,
                { _, yearPicked, monthPicked, dayPicked ->

                    tripDateEditText.setText(
                        "$dayPicked/$monthPicked/$yearPicked"
                    )
                },
                year,
                month,
                day
            )
            datePickerDialog.show()

        }
        return fragmentView
    }


}