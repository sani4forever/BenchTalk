package com.example.benchtalks.ui.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.benchtalks.databinding.ItemCardBinding
import com.example.benchtalks.models.PersonCard

class CardStackAdapter : ListAdapter<PersonCard, CardStackAdapter.CardViewHolder>(CardDiffCallback()) {

    class CardViewHolder(val binding: ItemCardBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = getItem(position) // ListAdapter сам управляет списком

        holder.binding.apply {
            tvName.text = "${item.name}, ${item.age}"
            tvAbout.text = item.about

            // Сбрасываем прозрачность оверлеев для переиспользуемых карточек
            leftOverlay.alpha = 0f
            rightOverlay.alpha = 0f
        }
    }

    // Класс для вычисления разницы между списками
    private class CardDiffCallback : DiffUtil.ItemCallback<PersonCard>() {
        override fun areItemsTheSame(oldItem: PersonCard, newItem: PersonCard): Boolean {
            // Сравниваем по уникальному ID
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PersonCard, newItem: PersonCard): Boolean {
            // Сравниваем все содержимое (data class сделает это за нас)
            return oldItem == newItem
        }
    }
}