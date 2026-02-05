package com.example.benchtalks.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.benchtalks.databinding.FragmentSwipeBinding
import com.example.benchtalks.models.PersonCard
import com.example.benchtalks.ui.recyclerview.CardStackAdapter
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.Duration
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting

class SwipeFragment : Fragment() { // Убрали наследование отсюда

    private var _binding: FragmentSwipeBinding? = null
    private val binding get() = _binding!!

    private lateinit var manager: CardStackLayoutManager
    // Адаптер теперь без параметров, так как это ListAdapter
    private val cardAdapter by lazy { CardStackAdapter() }

    // Создаем слушатель как отдельную переменную
    private val cardStackListener = object : CardStackListener {
        override fun onCardSwiped(direction: Direction?) {
            // Логика при свайпе
            when (direction) {
                Direction.Right -> Toast.makeText(context, "Лайк!", Toast.LENGTH_SHORT).show()
                Direction.Left -> Toast.makeText(context, "Пропуск", Toast.LENGTH_SHORT).show()
                else -> {}
            }

            // Если карточки кончились, можно что-то сделать
            if (manager.topPosition == cardAdapter.itemCount) {
                // Например, показать empty state
            }
        }

        override fun onCardRewound() {
            Toast.makeText(context, "Карточка вернулась", Toast.LENGTH_SHORT).show()
        }

        override fun onCardDragging(direction: Direction?, ratio: Float) {}
        override fun onCardAppeared(view: View?, position: Int) {}
        override fun onCardDisappeared(view: View?, position: Int) {}
        override fun onCardCanceled() {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentSwipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCardStack()
        setupButtons()

        // Загружаем данные в ListAdapter
        cardAdapter.submitList(getSampleData())
    }

    private fun setupCardStack() {
        // Передаем наш cardStackListener в конструктор менеджера
        manager = CardStackLayoutManager(requireContext(), cardStackListener).apply {
            setStackFrom(StackFrom.None)
            setVisibleCount(3)
            setTranslationInterval(8.0f)
            setScaleInterval(0.95f)
            setSwipeThreshold(0.3f)
            setMaxDegree(20.0f)
            setDirections(Direction.HORIZONTAL)
            setCanScrollVertical(true)
            setCanScrollHorizontal(true)
        }

        binding.cardStackView.layoutManager = manager
        binding.cardStackView.adapter = cardAdapter
    }

    private fun setupButtons() {
        binding.btnLike.setOnClickListener {
            val setting = SwipeAnimationSetting.Builder()
                .setDirection(Direction.Right)
                .setDuration(Duration.Normal.duration)
                .build()
            manager.setSwipeAnimationSetting(setting)
            binding.cardStackView.swipe()
        }

        binding.btnRefresh.setOnClickListener {
            binding.cardStackView.rewind()
        }
    }

    private fun getSampleData() = listOf(
        PersonCard(1, "Анна", 22, "Люблю кофе и Котлин"),
        PersonCard(2, "Иван", 25, "Разрабатываю игры на Unity"),
        PersonCard(3, "Мария", 21, "Дизайнер интерфейсов")
    )

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}