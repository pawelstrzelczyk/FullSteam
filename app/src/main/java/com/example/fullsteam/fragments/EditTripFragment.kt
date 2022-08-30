package com.example.fullsteam.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.icu.util.Calendar
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.fullsteam.R
import com.example.fullsteam.components.BrandSpinnerAdapter
import com.example.fullsteam.components.CurrencySpinnerAdapter
import com.example.fullsteam.components.StationAutoCompleteAdapter
import com.example.fullsteam.firebase.FirebaseHandler
import com.example.fullsteam.koleo.KoleoClient
import com.example.fullsteam.koleo.brands.Brand
import com.example.fullsteam.koleo.carriers.Carrier
import com.example.fullsteam.models.Trip
import com.example.fullsteam.portalpasazera.PPClient
import com.example.fullsteam.portalpasazera.Station
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.*

class EditTripFragment : Fragment() {
    private val DOCUMENT_ID = "documentId"
    private lateinit var sharedPref: SharedPreferences
    private var documentId: String? = null
    private lateinit var ppClient: PPClient
    private var brandsList: ArrayList<Brand> = arrayListOf()
    private var carriersList: ArrayList<Carrier> = arrayListOf()
    private lateinit var koleoClient: KoleoClient
    private lateinit var firebaseHandler: FirebaseHandler
    private var selectedBrand: Brand? = null
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

    private lateinit var tripBrand: String

    private val currencyList: ArrayList<String> = arrayListOf("PLN", "EUR")
    private lateinit var stations: ArrayList<Station>
    private lateinit var stationsLabels: ArrayList<String>


    private lateinit var tripEditFab: ExtendedFloatingActionButton
    private lateinit var brandSpinner: Spinner
    private lateinit var trainCarrierEditText: EditText
    private lateinit var trainNumberEditText: EditText
    private lateinit var currencySpinner: Spinner
    private lateinit var startAutoCompleteTextView: AutoCompleteTextView
    private lateinit var endAutoCompleteTextView: AutoCompleteTextView
    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var tripDateEditText: EditText
    private lateinit var tripStartTimeText: TextInputEditText
    private lateinit var tripEndTimeText: TextInputEditText
    private lateinit var trainNameEditText: TextInputEditText
    private lateinit var tripDistanceText: TextInputEditText
    private lateinit var tripDurationText: TextInputEditText
    private lateinit var checkboxesLayout: LinearLayout
    private lateinit var tripPriceText: TextInputEditText
    private lateinit var tripAvgSpeedText: TextInputEditText
    private lateinit var tripPricePerKm: TextInputEditText
    private lateinit var tripDelayText: TextInputEditText
    private lateinit var tripDepartureDelayText: TextInputEditText
    private lateinit var pkmCheckbox: CheckBox
    private lateinit var bikeCheckBox: CheckBox
    private lateinit var bikePriceLayout: TextInputLayout
    private lateinit var bikePriceEditText: TextInputEditText
    private lateinit var changeCheckBox: CheckBox
    private lateinit var sleepingCarCheckBox: CheckBox
    private lateinit var couchettePriceEditText: TextInputEditText
    private lateinit var couchettePriceLayout: TextInputLayout
    private lateinit var commentEditText: TextInputEditText

    private var database = FirebaseFirestore.getInstance()
    private lateinit var uId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            documentId = it.getString(DOCUMENT_ID)
        }
        sharedPref = requireActivity().getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE
        )
        ppClient = PPClient()
        koleoClient = KoleoClient()
        firebaseHandler = FirebaseHandler()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        uId = sharedPref.getString(
            getString(R.string.firebase_user_uid),
            "uid could not be retrieved"
        ).toString()
        var tripDurationSeconds: Long
        val fragmentView = inflater.inflate(R.layout.fragment_edit_trip, container, false)
        var tripToEdit: Trip
        trainNumberEditText = fragmentView.findViewById(R.id.edit_train_number_edit_text)
        trainNameEditText = fragmentView.findViewById(R.id.train_name_edit_text)
        tripEditFab = fragmentView.findViewById(R.id.trip_edit_fab)
        brandSpinner = fragmentView.findViewById(R.id.train_brand_spinner)
        trainCarrierEditText = fragmentView.findViewById(R.id.train_carrier_edit_text)
        currencySpinner = fragmentView.findViewById(R.id.trip_currency_option)
        checkboxesLayout = fragmentView.findViewById(R.id.checkboxes_layout)
        tripDateEditText = fragmentView.findViewById(R.id.trip_date_edit_text)
        tripEndTimeText = fragmentView.findViewById(R.id.trip_end_time_text)
        tripStartTimeText = fragmentView.findViewById(R.id.trip_start_time_text)
        startAutoCompleteTextView = fragmentView.findViewById(R.id.trip_start_station_text)
        endAutoCompleteTextView = fragmentView.findViewById(R.id.trip_end_station_text)
        tripDistanceText = fragmentView.findViewById(R.id.trip_distance_text)
        tripDurationText = fragmentView.findViewById(R.id.trip_duration_text)
        tripPriceText = fragmentView.findViewById(R.id.trip_price_text)
        tripAvgSpeedText = fragmentView.findViewById(R.id.trip_avg_speed_text)
        tripPricePerKm = fragmentView.findViewById(R.id.trip_price_per_km_text)
        tripDelayText = fragmentView.findViewById(R.id.trip_delay_text)
        tripDepartureDelayText = fragmentView.findViewById(R.id.trip_end_delay_text)
        pkmCheckbox = fragmentView.findViewById(R.id.pkm_checkbox)
        bikeCheckBox = fragmentView.findViewById(R.id.bike_checkbox)
        bikePriceEditText = fragmentView.findViewById(R.id.bike_price_text)
        bikePriceLayout = fragmentView.findViewById(R.id.bike_price_text_layout)
        changeCheckBox = fragmentView.findViewById(R.id.change_checkbox)
        sleepingCarCheckBox = fragmentView.findViewById(R.id.sleeping_car_checkbox)
        couchettePriceEditText = fragmentView.findViewById(R.id.couchette_price_text)
        couchettePriceLayout = fragmentView.findViewById(R.id.couchette_price_text_layout)
        commentEditText = fragmentView.findViewById(R.id.trip_comment_text)


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
            val tempBrands =
                brandsList.filter { brand -> brand.name in brandsToPromote }.toSet()
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
        brandSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                p1: View?,
                position: Int,
                id: Long
            ) {
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
                    val date = LocalDate.parse(
                        LocalDate.of(yearPicked, monthPicked + 1, dayPicked).toString(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")
                    )

                    tripDateEditText.setText(
                        //"$dayPicked/$monthPicked/$yearPicked"
                        date.toString()
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

        tripPriceText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                if (tripDistanceText.text?.isNotEmpty() == true && tripPriceText.text?.isNotEmpty() == true) {
                    tripPricePerKm.setText(
                        String.format(
                            Locale.US,
                            "%.2f",
                            (tripPriceText.text.toString()
                                .toDouble() / tripDistanceText.text.toString()
                                .toDouble())
                        ),
                        TextView.BufferType.EDITABLE
                    )
                }
            }

        }

        tripDelayText.onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) {
                if (tripDelayText.text.toString().isNotEmpty()) {
                    tripDurationSeconds = tripDelayText.text.toString().toLong() * 60
                    try {
                        tripEndTimeText.setText(
                            LocalTime.parse(tripEndTimeText.text.toString())
                                .plus(Duration.ofSeconds(tripDurationSeconds))
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
                    } catch (e: DateTimeParseException) {
                        Toast.makeText(
                            requireContext(),
                            "Cannot change arrival time when it's empty!",
                            Toast.LENGTH_LONG
                        ).show()
                        tripDelayText.setText(0.toString(), TextView.BufferType.EDITABLE)
                    }

                    if (tripDistanceText.text?.isNotEmpty() == true && tripDurationText.text?.isNotEmpty() == true) {
                        tripAvgSpeedText.setText(
                            String.format(
                                Locale.US,
                                "%.2f", (tripDistanceText.text.toString().toDouble() * 1000 /
                                        Duration.ofSeconds(
                                            LocalTime.MIN.until(
                                                LocalTime.parse(
                                                    tripDurationText.text.toString()
                                                ), ChronoUnit.SECONDS
                                            )
                                        ).seconds * 3.6)
                            ),
                            TextView.BufferType.EDITABLE
                        )
                    }
                } else {
                    tripDelayText.setText(
                        "0",
                        TextView.BufferType.EDITABLE
                    )
                    tripDelayText.setSelection(tripDelayText.length())
                }
            }
        }
        tripDepartureDelayText.onFocusChangeListener =
            View.OnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) {
                    if (tripDepartureDelayText.text.toString().isNotEmpty()) {
                        tripDurationSeconds =
                            tripDepartureDelayText.text.toString().toLong() * 60
                        Log.d("tripDurationSeconds", tripDurationSeconds.toString())
                        try {
                            tripStartTimeText.setText(
                                LocalTime.parse(tripStartTimeText.text.toString())
                                    .plus(Duration.ofSeconds(tripDurationSeconds))
                                    .toString(),
                                TextView.BufferType.EDITABLE
                            )
                            tripDurationText.setText(
                                LocalTime.MIN.plus(
                                    Duration.ofMinutes(
                                        LocalTime.parse(tripStartTimeText.text.toString())
                                            .until(
                                                LocalTime.parse(tripEndTimeText.text.toString()),
                                                ChronoUnit.MINUTES
                                            )
                                    )
                                ).toString(),
                                TextView.BufferType.EDITABLE
                            )
                        } catch (e: DateTimeParseException) {
                            Toast.makeText(
                                requireContext(),
                                "Cannot change arrival time when it's empty!",
                                Toast.LENGTH_LONG
                            ).show()
                            tripDepartureDelayText.setText(
                                0.toString(),
                                TextView.BufferType.EDITABLE
                            )
                        }

                        if (tripDistanceText.text?.isNotEmpty() == true && tripDurationText.text?.isNotEmpty() == true) {
                            tripAvgSpeedText.setText(
                                String.format(
                                    Locale.US,
                                    "%.2f",
                                    (tripDistanceText.text.toString().toDouble() * 1000 /
                                            Duration.ofSeconds(
                                                LocalTime.MIN.until(
                                                    LocalTime.parse(
                                                        tripDurationText.text.toString()
                                                    ), ChronoUnit.SECONDS
                                                )
                                            ).seconds * 3.6)
                                ),
                                TextView.BufferType.EDITABLE
                            )
                        }
                    } else {
                        tripDepartureDelayText.setText(
                            "0",
                            TextView.BufferType.EDITABLE
                        )
                        tripDepartureDelayText.setSelection(tripDepartureDelayText.length())
                    }

                }
            }

        bikeCheckBox.setOnCheckedChangeListener { _, b ->
            if (b) {
                bikePriceEditText.visibility = View.VISIBLE
                bikePriceLayout.visibility = View.VISIBLE
            } else {
                bikePriceEditText.visibility = View.GONE
                bikePriceLayout.visibility = View.GONE
            }
            checkboxesLayout.invalidate()
        }

        sleepingCarCheckBox.setOnCheckedChangeListener { _, b ->
            if (b) {
                couchettePriceEditText.visibility = View.VISIBLE
                couchettePriceLayout.visibility = View.VISIBLE
            } else {
                couchettePriceEditText.visibility = View.GONE
                couchettePriceLayout.visibility = View.GONE
            }
            checkboxesLayout.invalidate()
        }





        documentId?.let {
            database.collection("users").document(uId).collection("trips")
                .document(documentId.toString())
                .get().addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot != null) {
                        tripToEdit = documentSnapshot.toObject(Trip::class.java)!!

                        tripDateEditText.setText(
                            tripToEdit.dateTime,
                            TextView.BufferType.EDITABLE
                        )
                        trainNameEditText.setText(
                            tripToEdit.trainName,
                            TextView.BufferType.EDITABLE
                        )
                        trainNumberEditText.setText(
                            tripToEdit.trainNumber.toString(),
                            TextView.BufferType.EDITABLE
                        )
                        startAutoCompleteTextView.setText(
                            tripToEdit.startStation,
                            TextView.BufferType.EDITABLE
                        )
                        endAutoCompleteTextView.setText(
                            tripToEdit.endStation,
                            TextView.BufferType.EDITABLE
                        )
                        tripStartTimeText.setText(
                            tripToEdit.startTime,
                            TextView.BufferType.EDITABLE
                        )
                        tripEndTimeText.setText(
                            tripToEdit.endTime,
                            TextView.BufferType.EDITABLE
                        )
                        tripDurationText.setText(
                            tripToEdit.tripTimeInMinutes.toString(),
                            TextView.BufferType.EDITABLE
                        )
                        tripDepartureDelayText.setText(
                            tripToEdit.departureDelay.toString(),
                            TextView.BufferType.EDITABLE
                        )
                        tripDelayText.setText(
                            tripToEdit.delay.toString(),
                            TextView.BufferType.EDITABLE
                        )
                        tripDistanceText.setText(
                            tripToEdit.kmDistance.toString(),
                            TextView.BufferType.EDITABLE
                        )
                        tripPriceText.setText(
                            tripToEdit.price.toString(),
                            TextView.BufferType.EDITABLE
                        )
                        tripAvgSpeedText.setText(
                            tripToEdit.avgSpeed.toString(),
                            TextView.BufferType.EDITABLE
                        )
                        tripPricePerKm.setText(
                            tripToEdit.pricePerKm.toString(),
                            TextView.BufferType.EDITABLE
                        )
                        pkmCheckbox.isChecked = tripToEdit.isPKM
                        bikeCheckBox.isChecked = tripToEdit.hasBike
                        bikePriceEditText.setText(
                            tripToEdit.bikePrice.toString(),
                            TextView.BufferType.EDITABLE
                        )
                        sleepingCarCheckBox.isChecked = tripToEdit.isSleepingCar
                        couchettePriceEditText.setText(
                            tripToEdit.couchettePrice.toString(),
                            TextView.BufferType.EDITABLE
                        )
                        changeCheckBox.isChecked = tripToEdit.hasChange
                        commentEditText.setText(tripToEdit.comment, TextView.BufferType.EDITABLE)
                        tripBrand = tripToEdit.trainBrand
                        koleoClient.getBrands().observeForever {
                            for (brand in it[0].brands) {
                                brandsList.add(brand)
                            }
                            val tempBrands =
                                brandsList.filter { brand -> brand.name == tripBrand }.toSet()
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

                    }
                }
        }

        tripEditFab.setOnClickListener {

            if (documentId != null) {
                viewLifecycleOwner.lifecycleScope.launch {
                    firebaseHandler.editTrip(
                        uId,
                        documentId!!,
                        requireContext(),
                        tripDateEditText.text.toString(),
                        selectedBrand?.name.toString(),
                        trainNumberEditText.text.toString().toInt(),
                        trainNameEditText.text.toString(),
                        trainCarrierEditText.text.toString(),
                        startAutoCompleteTextView.text.toString(),
                        tripStartTimeText.text.toString(),
                        endAutoCompleteTextView.text.toString(),
                        tripEndTimeText.text.toString(),
                        tripDistanceText.text.toString().toInt(),
                        tripDurationText.text.toString().toInt(),
                        tripPriceText.text.toString().toDouble(),
                        currencySpinner.selectedItem.toString(),
                        tripPricePerKm.text.toString().toDouble(),
                        tripAvgSpeedText.text.toString().toDouble(),
                        changeCheckBox.isChecked,
                        bikeCheckBox.isChecked,
                        bikePriceEditText.text.toString().toDouble(),
                        pkmCheckbox.isChecked,
                        sleepingCarCheckBox.isChecked,
                        couchettePriceEditText.text.toString().toDouble(),
                        tripDelayText.text.toString().toInt(),
                        tripDepartureDelayText.text.toString().toInt(),
                        commentEditText.text.toString()
                    )
                }
            }

            requireView().findNavController().popBackStack()
        }

        return fragmentView
    }


}