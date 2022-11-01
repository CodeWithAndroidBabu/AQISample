package co.android.helper.hkaqi.ui.home.map.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import co.android.helper.hkaqi.R
import co.android.helper.hkaqi.data.AppConstants
import co.android.helper.hkaqi.data.model.AQISearchModel
import co.android.helper.hkaqi.databinding.ViewHolderAqiListLayoutBinding

class AQIListAdapter(
    private val aqiList: MutableList<AQISearchModel.Data>,
    val listener: (AQISearchModel.Data) -> Unit
) :
    RecyclerView.Adapter<AQIListAdapter.AQIViewHolder>() {
    private lateinit var context: Context
    var apiRandom = (1..50).random()

    inner class AQIViewHolder(val binding: ViewHolderAqiListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.rootView.setOnClickListener {
                listener.invoke(aqiList[layoutPosition])
            }
        }

        private fun getAQIName(aqi: Int) {
            when (aqi) {
                in 0..50 -> {
                    binding.rlBg.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_aqi_good)
                    binding.ivMarkerIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon_aqi_good
                        )
                    )
                }
                in 51..100 -> {
                    binding.rlBg.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_aqi_moderate)
                    binding.ivMarkerIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon_aqi_moderate
                        )
                    )
                }
                in 101..150 -> {
                    binding.rlBg.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_aqi_moderate_unhealthy)
                    binding.ivMarkerIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon_aqi_moderate_unhealthy
                        )
                    )
                }
                in 151..200 -> {
                    binding.rlBg.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_aqi_unhealthy)
                    binding.ivMarkerIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon_aqi_unhealthy
                        )
                    )
                }
                in 201..300 -> {
                    binding.rlBg.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_aqi_very_unhealthy)
                    binding.ivMarkerIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon_aqi_very_unhealthy
                        )
                    )
                }
                else -> {
                    binding.rlBg.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_aqi_haradous)
                    binding.ivMarkerIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.icon_aqi_hazardous
                        )
                    )
                }
            }
            binding.tvAQINo.text = "$aqi"
        }

        private fun getAQILevel(aqi: Int) {
            when (aqi) {
                in 0..50 -> {
                    binding.tvAQILevelName.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_aqi_good)
                    binding.tvAQILevelName.text = "Good"
                }
                in 51..100 -> {
                    binding.tvAQILevelName.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_aqi_moderate)
                    binding.tvAQILevelName.text = "Moderate"
                }
                in 101..150 -> {
                    binding.tvAQILevelName.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_aqi_moderate_unhealthy)
                    binding.tvAQILevelName.text = "Unhealthy-Moderate"
                }
                in 151..200 -> {
                    binding.tvAQILevelName.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_aqi_unhealthy)
                    binding.tvAQILevelName.text = "Unhealthy"
                }
                in 201..300 -> {
                    binding.tvAQILevelName.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_aqi_very_unhealthy)
                    binding.tvAQILevelName.text = "Very-Unhealthy"
                }
                else -> {
                    binding.tvAQILevelName.background =
                        ContextCompat.getDrawable(context, R.drawable.bg_aqi_haradous)
                    binding.tvAQILevelName.text = "Hazardous"
                }
            }

        }

        fun bind(data: AQISearchModel.Data) {
            try {
                getAQIName(data.aqi.toInt())
            } catch (e: Exception) {
                /**
                 * If format exceptions error, must show UI as unavailable by using place holder
                 * */
                binding.rlBg.background =
                    ContextCompat.getDrawable(context, R.drawable.bg_grey_round)
                binding.ivMarkerIcon.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.img_place_holder
                    )
                )
                binding.tvAQINo.text = "---"
            }
            try {
                getAQILevel(data.aqi.toInt())
            } catch (e: Exception) {
                /**
                 * If format exceptions error, must show UI as unavailable by using gray bg
                 * */
                binding.tvAQILevelName.background =
                    ContextCompat.getDrawable(context, R.drawable.bg_grey_round)
                binding.tvAQILevelName.text = "---"
            }

            binding.tvAQIName.text = data.station.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AQIViewHolder {
        context = parent.context
        val binding = DataBindingUtil.inflate<ViewHolderAqiListLayoutBinding>(
            LayoutInflater.from(context),
            R.layout.view_holder_aqi_list_layout, parent, false
        )
        return AQIViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AQIViewHolder, position: Int) {
        holder.bind(aqiList[position])
    }

    override fun getItemCount(): Int = aqiList.size

    fun refreshList(newList: List<AQISearchModel.Data>, shouldClearList: Boolean = true) {
        aqiList.clear()
        aqiList.addAll(newList)
        notifyDataSetChanged()
    }

    fun clearList() {
        aqiList.clear()
        notifyDataSetChanged()
    }

    fun getDefaultMauritiusFirstItem() = mutableListOf<Double>().apply {
        add(-20.166667)
        add(57.499997)
    }

    fun setDefaultItemsForMauritius() {
        //First Item Added
        val itemOneStation = AQISearchModel.Data.Station(
            "africa",
            getDefaultMauritiusFirstItem(),
            AppConstants.Value.MAURITIUS_PORT_LOUIS,
            ""
        )
        val itemOne =
            AQISearchModel.Data("$apiRandom", itemOneStation, AQISearchModel.Data.Time(), 11)

        //Second Item Added
        val itemTwoLatLng = mutableListOf<Double>().apply {
            add(-20.263811)
            add(57.479100)
        }

        val itemTwoStation =
            AQISearchModel.Data.Station(AppConstants.Value.AFRICA, itemTwoLatLng, AppConstants.Value.MAURITIUS_QUATRE_BORNES, "")
        val itemTwo = AQISearchModel.Data(
            "${(50..102).random()}",
            itemTwoStation,
            AQISearchModel.Data.Time(),
            15
        )

        val defaultItems = mutableListOf<AQISearchModel.Data>().apply {
            add(itemOne)
            add(itemTwo)
        }

        aqiList.clear()
        aqiList.addAll(defaultItems)
        notifyDataSetChanged()
    }
}