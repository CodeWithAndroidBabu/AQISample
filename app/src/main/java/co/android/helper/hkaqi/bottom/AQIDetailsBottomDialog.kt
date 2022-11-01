package co.android.helper.hkaqi.bottom

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import co.android.helper.hkaqi.R
import co.android.helper.hkaqi.common.DialogMsg
import co.android.helper.hkaqi.data.AppConstants
import co.android.helper.hkaqi.data.model.AQIDetailsModel
import co.android.helper.hkaqi.data.model.MAURITIUS
import co.android.helper.hkaqi.databinding.BottomLayoutAqiDetailsBinding
import co.android.helper.hkaqi.utils.makeVisible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.*


class AQIDetailsBottomDialog : BottomSheetDialogFragment() {

    private lateinit var binding: BottomLayoutAqiDetailsBinding
    private var aqiDetailsModel: AQIDetailsModel? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        arguments?.let {
            aqiDetailsModel =
                it.getSerializable(AppConstants.Keys.SERIALIZABLE_DATA) as AQIDetailsModel
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            BottomSheetDialogFragment.STYLE_NORMAL,
            co.android.helper.hkaqi.R.style.MenuBottomSheetDialog
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        /* view.viewTreeObserver.addOnGlobalLayoutListener {
             val d = dialog as BottomSheetDialog
             val bottomSheet: FrameLayout? =
                 d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
             val behavior: BottomSheetBehavior<View?>? =
                 bottomSheet?.let { BottomSheetBehavior.from(it) }
             //behavior?.peekHeight = 1000

             //behavior?.state = BottomSheetBehavior.STATE_EXPANDED
         }*/
        val d = dialog as BottomSheetDialog
        val bottomSheet: FrameLayout? =
            d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
        val behavior: BottomSheetBehavior<View?>? =
            bottomSheet?.let { BottomSheetBehavior.from(it) }

        binding.rootView.post {
            behavior?.peekHeight =
                (binding.viewHolderLayout.rootView.measuredHeight - binding.rootView.measuredHeight) + binding.tvCityName.measuredHeight - 100
            Log.d(
                "fahjfhjasfsa",
                "onViewCreated: ${binding.rootView.measuredHeight} -- ${binding.viewHolderLayout.rootView.measuredHeight} result ${binding.viewHolderLayout.rootView.measuredHeight - binding.rootView.measuredHeight}"
            )
        }


        d.window?.setDimAmount(0.0f)
        setAQIDetails(aqiDetailsModel)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        dialog?.setCanceledOnTouchOutside(false)
        binding = DataBindingUtil.inflate<BottomLayoutAqiDetailsBinding>(
            inflater,
            co.android.helper.hkaqi.R.layout.bottom_layout_aqi_details,
            container,
            false
        )

        binding.ivClose.setOnClickListener {
            dismiss()
        }
        binding.llInfoAQLevel.setOnClickListener {
            DialogMsg.showMsg(
                requireContext(),
                title = "Info",
                msg = "The table below defines the Air Quality Index scale as defined by the US-EPA 2016 standard."
            ) {}
        }
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = AQIDetailsBottomDialog()
    }

    fun addDialogEventListener(dialogEventListener: DialogEventListener) {
        //  mDialogEventListener = dialogEventListener
    }

    interface DialogEventListener {
        fun onItemClicked(price: String, description: String)
        fun onDialogDismiss()
    }

    /* override fun onDetach() {
         mDialogEventListener = null
         super.onDetach()
     }

     override fun onDismiss(dialog: DialogInterface) {
         super.onDismiss(dialog)
         if (mDialogEventListener != null) {
             mDialogEventListener!!.onDialogDismiss()
         }
     }*/

    private fun setAQIDetails(aqiDetailsModel: AQIDetailsModel?) {
        aqiDetailsModel?.let {
            binding.tvCityName.text = it.cityName
            try {
                setAqiNoAndIcon(it.aqi.toInt())
                Log.d("fabjasss", "${it.defaultCityName} setAQIDetails:${getAQIName(it.aqi.toInt())} ")
                binding.tvAQIName.text = getAQIName(it.aqi.toInt())
                binding.llAqiDetails.makeVisible(true)
                when (it.defaultCityName) {
                    MAURITIUS.PORT_LOUIS.name -> {
                        binding.llAqiDetails.makeVisible(false)
                        Log.d("fabjasss", "setAQIDetails:22${getAQIName(it.aqi.toInt())} ")
                        setRemainingDetails("PORT LOUIS GOVT SCHOOL",getAQIName(it.aqi.toInt()),"Port Louis","Rural")
                    }
                    MAURITIUS.QUATRE_BORNES.name -> {
                        Log.d("fabjasss", "setAQIDetails:11${getAQIName(it.aqi.toInt())} ")
                        binding.llAqiDetails.makeVisible(false)
                        setRemainingDetails("CHOOROMONEE GOVT SCHOOL",getAQIName(it.aqi.toInt()),"Quatre Bornes","Urban")
                    }
                    else -> {
                        binding.llAqiDetails.makeVisible(true)
                    }
                }
            } catch (e: Exception) {
                binding.llAqiBg.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_grey_round)
                binding.ivAQILevelIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.img_place_holder
                    )
                )
                binding.tvAQINo.text = "-"
                binding.tvAQIName.text = "No Data Available"
                binding.llAqiDetails.makeVisible(true)
            }
        }
    }

    private fun getMauritiusTime():String{
        val tz: TimeZone = TimeZone.getTimeZone("GMT+4")
        val c = Calendar.getInstance(tz).time
        return c.toString()
    }

    private fun setRemainingDetails(aqiSubName: String,aqiLevelName: String,
    location: String,area:String){
        binding.tvAQISubName.text = aqiSubName
        binding.tvAQILevelName.text = aqiLevelName
        binding.tvAQILevelName.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                getAQILevelColor(binding.tvAQINo.text.toString().toInt())
            )
        )
        binding.tvAQIDate.text = getMauritiusTime()
        binding.tvAQILocation.text = location
        binding.tvAQIAreaName.text = area
    }

    private fun setAqiNoAndIcon(aqi: Int) {
        when (aqi) {
            in 0..50 -> {
                binding.llAqiBg.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_aqi_good)
                binding.ivAQILevelIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.icon_aqi_good
                    )
                )
                binding.tvAQIName.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.aqi_good
                    )
                )
            }
            in 51..100 -> {
                binding.llAqiBg.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_aqi_moderate)
                binding.ivAQILevelIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.icon_aqi_moderate
                    )
                )
                binding.tvAQIName.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.aqi_moderate
                    )
                )
            }
            in 101..150 -> {
                binding.llAqiBg.background =
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.bg_aqi_moderate_unhealthy
                    )
                binding.ivAQILevelIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.icon_aqi_moderate_unhealthy
                    )
                )
                binding.tvAQIName.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.aqi_moderate_unhealthy
                    )
                )
            }
            in 151..200 -> {
                binding.llAqiBg.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_aqi_unhealthy)
                binding.ivAQILevelIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.icon_aqi_unhealthy
                    )
                )
                binding.tvAQIName.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.aqi_unhealthy
                    )
                )
            }
            in 201..300 -> {
                binding.llAqiBg.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_aqi_very_unhealthy)
                binding.ivAQILevelIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.icon_aqi_very_unhealthy
                    )
                )
                binding.tvAQIName.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.aqi_very_unhealthy
                    )
                )
            }
            else -> {
                binding.llAqiBg.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.bg_aqi_haradous)
                binding.ivAQILevelIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.icon_aqi_hazardous
                    )
                )
                binding.tvAQIName.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.aqi_hazardous
                    )
                )
            }
        }
        binding.tvAQINo.text = "$aqi"
    }

    private fun getAQIName(aqi: Int): String {
        when (aqi) {
            in 0..50 -> {
                return "Good"
            }
            in 51..100 -> {
                return "Moderate"
            }
            in 101..150 -> {
                return "Unhealthy-Moderate"
            }
            in 151..200 -> {
                return "Unhealthy"
            }
            in 201..300 -> {
                return "Very-Unhealthy"
            }
            else -> {
                return "Hazardous"
            }
        }
    }

    private fun getAQILevelColor(aqi: Int): Int {
        when (aqi) {
            in 0..50 -> {
                return R.color.aqi_good
            }
            in 51..100 -> {
                return R.color.aqi_moderate
            }
            in 101..150 -> {
                return R.color.aqi_moderate_unhealthy
            }
            in 151..200 -> {
                return R.color.aqi_unhealthy
            }
            in 201..300 -> {
                return R.color.aqi_very_unhealthy
            }
            else -> {
                return R.color.aqi_hazardous
            }
        }
    }
}