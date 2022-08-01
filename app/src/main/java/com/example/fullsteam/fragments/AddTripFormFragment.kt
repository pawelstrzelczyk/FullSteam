package com.example.fullsteam.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import com.example.fullsteam.R
import com.example.fullsteam.components.BrandSpinnerAdapter
import com.example.fullsteam.components.CurrencySpinnerAdapter
import com.example.fullsteam.components.StationAutoCompleteAdapter
import com.example.fullsteam.koleo.KoleoClient
import com.example.fullsteam.koleo.brands.Brand
import com.example.fullsteam.koleo.carriers.Carrier
import com.example.fullsteam.portalpasazera.PPClient
import com.example.fullsteam.portalpasazera.Station
import com.google.android.material.textfield.TextInputEditText
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class AddTripFormFragment : Fragment() {
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var tripDateEditText: EditText
    private lateinit var brandSpinner: Spinner
    private lateinit var trainNumberEditText: EditText
    private var brandsList: ArrayList<Brand> = arrayListOf()
    private var carriersList: ArrayList<Carrier> = arrayListOf()
    private var ppClient = PPClient()
    private lateinit var startAutoCompleteTextView: AutoCompleteTextView
    private lateinit var endAutoCompleteTextView: AutoCompleteTextView
    private lateinit var stations: ArrayList<Station>
    private lateinit var stationsLabels: ArrayList<String>
    private lateinit var tripStartTimeText: TextInputEditText
    private lateinit var tripEndTimeText: TextInputEditText
    private lateinit var trainNameEditText: TextInputEditText
    private lateinit var tripDistanceText: TextInputEditText
    private lateinit var tripDurationText: TextInputEditText
    private lateinit var tripPriceText: TextInputEditText
    private lateinit var tripAvgSpeedText: TextInputEditText
    private lateinit var tripPricePerKm: TextInputEditText
    private lateinit var tripDelayText: TextInputEditText

    private val currencyList: ArrayList<String> = arrayListOf("PLN", "EUR")
    private lateinit var currencySpinner: Spinner
    private lateinit var koleoClient: KoleoClient
    private lateinit var trainCarrierEditText: EditText
    private val brandsToPromote: List<String> = listOf(
        "KW",
        "IC",
        "KD",
        "REG",
        "TLK",
        "SKMT",
        "KM",
        "KS",
        "SKM",
        "WKD",
        "KMŁ",
        "EIC",
        "EIP"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        koleoClient = KoleoClient()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentView = inflater.inflate(R.layout.fragment_add_trip_form, container, false)
        var selectedBrand: Brand? = Brand()
        var selectedStartStation: Station?
        var selectedEndStation: Station?
        var tripDurationSeconds: Long = Long.MAX_VALUE
        tripDateEditText = fragmentView.findViewById(R.id.trip_date_edit_text)
        tripEndTimeText = fragmentView.findViewById(R.id.trip_end_time_text)
        tripStartTimeText = fragmentView.findViewById(R.id.trip_start_time_text)
        tripStartTimeText.inputType = InputType.TYPE_NULL
        tripEndTimeText.inputType = InputType.TYPE_NULL
        brandSpinner = fragmentView.findViewById(R.id.train_brand_spinner)
        currencySpinner = fragmentView.findViewById(R.id.trip_currency_option)
        trainCarrierEditText = fragmentView.findViewById(R.id.train_carrier_edit_text)
        startAutoCompleteTextView = fragmentView.findViewById(R.id.trip_start_station_text)
        endAutoCompleteTextView = fragmentView.findViewById(R.id.trip_end_station_text)
        trainNameEditText = fragmentView.findViewById(R.id.train_name_edit_text)
        tripDateEditText.inputType = InputType.TYPE_NULL
        trainNumberEditText = fragmentView.findViewById(R.id.train_number_edit_text)
        tripDistanceText = fragmentView.findViewById(R.id.trip_distance_text)
        tripDurationText = fragmentView.findViewById(R.id.trip_duration_text)
        tripPriceText = fragmentView.findViewById(R.id.trip_price_text)
        tripAvgSpeedText = fragmentView.findViewById(R.id.trip_avg_speed_text)
        tripPricePerKm = fragmentView.findViewById(R.id.trip_price_per_km_text)
        tripDelayText = fragmentView.findViewById(R.id.trip_delay_text)
        var brandAdapter = BrandSpinnerAdapter(
            requireContext(),
            brandsList.sortedBy { it.name })
        val currencyAdapter: ArrayAdapter<String> =
            CurrencySpinnerAdapter(requireContext(), currencyList)
        koleoClient.getCarriers().observeForever {
            for (carrier in it[0].carriers) {
                carriersList.add(carrier)
            }
        }
        koleoClient.getBrands().observeForever {
            for (brand in it[0].brands) {
                brandsList.add(brand)
            }
            val tempBrands = brandsList.filter { brand -> brand.name in brandsToPromote }.toSet()
            brandsList.removeAll(tempBrands)
            brandsToPromote.reversed().forEach { brandToPromote ->
                tempBrands.find { tempBrand -> tempBrand.name == brandToPromote }
                    ?.let { it1 -> brandsList.add(0, it1) }
            }
            brandAdapter.notifyDataSetChanged()
        }

        brandAdapter = BrandSpinnerAdapter(
            requireContext(),
            brandsList
        )



        brandSpinner.adapter = brandAdapter
        brandSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, id: Long) {
                if (p0 != null) {
                    selectedBrand = p0.getItemAtPosition(position) as Brand?

                    trainCarrierEditText.setText(
                        carriersList.find { carrier ->
                            carrier.id == (selectedBrand?.carrier_id ?: 8)
                        }?.name ?: "unknown",
                        TextView.BufferType.EDITABLE
                    )
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        currencySpinner.adapter = currencyAdapter
        tripStartTimeText.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR)
            val minute = calendar.get(Calendar.MINUTE)
            timePickerDialog = TimePickerDialog(
                fragmentView.context,
                { _, i, i2 ->
                    tripStartTimeText.setText(buildString {
                        append(i)
                        append(String.format(":%02d", i2))
                    })
                },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }
        tripEndTimeText.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR)
            val minute = calendar.get(Calendar.MINUTE)
            timePickerDialog = TimePickerDialog(
                fragmentView.context,
                { _, i, i2 ->
                    tripEndTimeText.setText(buildString {
                        append(i)
                        append(String.format(":%02d", i2))
                    })
                },
                hour,
                minute,
                true
            )
            timePickerDialog.show()
        }
        tripDateEditText.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val month = calendar.get(Calendar.MONTH)
            val year = calendar.get(Calendar.YEAR)
            datePickerDialog = DatePickerDialog(
                fragmentView.context,
                { _, yearPicked, monthPicked, dayPicked ->
                    tripDateEditText.setText(
                        //"$dayPicked/$monthPicked/$yearPicked"
                        LocalDate.parse(
                            LocalDate.of(yearPicked, monthPicked + 1, dayPicked).toString(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        ).toString()
                    )

                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
        stations = arrayListOf()
        stationsLabels = arrayListOf()
        var ppAdapter = StationAutoCompleteAdapter(requireContext(), stationsLabels)
        startAutoCompleteTextView.setAdapter(ppAdapter)
        endAutoCompleteTextView.setAdapter(ppAdapter)
        startAutoCompleteTextView.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                stationsLabels.clear()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                stationsLabels.clear()
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    ppClient.getStations(p0.toString()).observeForever {
                        if (stationsLabels.isEmpty()) {
                            it[0].forEach { station ->
                                stations.add(station)
                                stationsLabels.add(station.Nazwa)
                            }
                        }
                        ppAdapter.notifyDataSetChanged()
                    }
                    ppAdapter = StationAutoCompleteAdapter(requireContext(), stationsLabels)
                    startAutoCompleteTextView.setAdapter(ppAdapter)

                }
            }
        })
        endAutoCompleteTextView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                stationsLabels.clear()
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                stationsLabels.clear()
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0.toString().isNotEmpty()) {
                    ppClient.getStations(p0.toString()).observeForever {

                        if (stationsLabels.isEmpty()) {
                            it[0].forEach { station ->
                                stations.add(station)
                                stationsLabels.add(station.Nazwa)
                            }
                        }
                        ppAdapter.notifyDataSetChanged()
                    }
                    ppAdapter = StationAutoCompleteAdapter(requireContext(), stationsLabels)
                    endAutoCompleteTextView.setAdapter(ppAdapter)


                }
            }
        })
        var number = 0


        trainNameEditText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                if (tripDateEditText.text.isNotEmpty() && trainNumberEditText.text.isNotEmpty() && trainNameEditText.text?.isNotEmpty() == true && brandSpinner.selectedItem != null) {
                    selectedBrand?.let { brand ->
                        koleoClient.getTrainCalendars(

                            brand.name.trim(),
                            Integer.parseInt(trainNumberEditText.text.toString().trim()),
                            trainNameEditText.text.toString().trim()
                        ).observeForever { calendarResponse ->

                            if (calendarResponse.isNotEmpty()) {
                                number =
                                    calendarResponse[0].train_calendars[0].date_train_map[tripDateEditText.text.toString()]!!

                                koleoClient.getTrain(number).observeForever {
                                    if (startAutoCompleteTextView.text.isNotEmpty()) {
                                        val hour =
                                            it[0].stops.find { stop -> stop.station_name == startAutoCompleteTextView.text.toString() }?.departure?.hour
                                        val minute =
                                            it[0].stops.find { stop -> stop.station_name == startAutoCompleteTextView.text.toString() }?.departure?.minute
                                        tripStartTimeText.setText(
                                            "${String.format("%02d", hour)}:${String.format("%02d", minute)}",
                                            TextView.BufferType.EDITABLE
                                        )
                                    }
                                    if (endAutoCompleteTextView.text.isNotEmpty()) {
                                        val hour =
                                            it[0].stops.find { stop -> stop.station_name == endAutoCompleteTextView.text.toString() }?.departure?.hour
                                        val minute =
                                            it[0].stops.find { stop -> stop.station_name == endAutoCompleteTextView.text.toString() }?.departure?.minute
                                        tripEndTimeText.setText(
                                            "${String.format("%02d", hour)}:${String.format("%02d", hour)}",
                                            TextView.BufferType.EDITABLE
                                        )
                                    }
                                    if (startAutoCompleteTextView.text.isNotEmpty() && endAutoCompleteTextView.text.isNotEmpty()) {
                                        val startTime = tripStartTimeText.text.toString()
                                        val endTime = tripEndTimeText.text.toString()
                                        val startDistance =
                                            it[0].stops.find { stop -> stop.station_name == startAutoCompleteTextView.text.toString() }?.distance!!
                                        val endDistance =
                                            it[0].stops.find { stop -> stop.station_name == endAutoCompleteTextView.text.toString() }?.distance!!

                                        tripDurationSeconds =
                                            Duration.ofSeconds(
                                                LocalTime.parse(startTime).until(
                                                    LocalTime.parse(endTime),
                                                    ChronoUnit.SECONDS
                                                )
                                            ).seconds

                                        tripDurationText.setText(
                                            LocalTime.MIN.plus(
                                                Duration.ofMinutes(
                                                    LocalTime.parse(startTime).until(
                                                        LocalTime.parse(endTime),
                                                        ChronoUnit.MINUTES
                                                    )
                                                )
                                            ).toString(),
                                            TextView.BufferType.EDITABLE

                                        )

                                        tripDistanceText.setText(
                                            (endDistance / 1000 - startDistance / 1000).toString(),
                                            TextView.BufferType.EDITABLE
                                        )

                                        tripAvgSpeedText.setText(
                                            ((endDistance - startDistance) /
                                                    LocalTime.parse(startTime).until(
                                                        LocalTime.parse(endTime),
                                                        ChronoUnit.SECONDS
                                                    ) * 3.6).toString(),
                                            TextView.BufferType.EDITABLE
                                        )
                                    }


                                }
                            }

                        }

                    }
                }


            }
        }
        tripPriceText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                if (tripDistanceText.text?.isNotEmpty() == true && tripPriceText.text?.isNotEmpty() == true) {
                    tripPricePerKm.setText(
                        (tripPriceText.text.toString().toDouble() / tripDistanceText.text.toString()
                            .toDouble()).toString(),
                        TextView.BufferType.EDITABLE
                    )
                }
            }

        }
        tripDelayText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                if (!tripDelayText.text.isNullOrEmpty()){
                    tripDurationSeconds += tripDelayText.text.toString().toLong() * 60
                    tripEndTimeText.setText(
                        LocalTime.parse(tripEndTimeText.text.toString())
                            .plus(Duration.ofMinutes(tripDelayText.text.toString().toLong()))
                            .toString(),
                        TextView.BufferType.EDITABLE
                    )
                    tripDurationText.setText(
                        LocalTime.MIN.plus(
                            Duration.ofMinutes(
                                LocalTime.parse(tripStartTimeText.text.toString()).until(
                                    LocalTime.parse(tripEndTimeText.text.toString()),
                                    ChronoUnit.MINUTES
                                )
                            )
                        ).toString(),
                        TextView.BufferType.EDITABLE
                    )
                    if (tripDistanceText.text?.isNotEmpty() == true && tripDurationText.text?.isNotEmpty() == true) {
                        tripAvgSpeedText.setText(
                            (tripDistanceText.text.toString().toDouble() * 1000 /
                                    tripDurationSeconds * 3.6).toString(),
                            TextView.BufferType.EDITABLE
                        )
                    }
                }


            }

        }
        return fragmentView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


    }
}