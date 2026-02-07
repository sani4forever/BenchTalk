package com.example.benchtalks.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.example.benchtalks.databinding.FragmentSwipeBinding
import com.example.benchtalks.ui.recyclerview.CardStackAdapter
import com.example.benchtalks.viewmodels.SwipeViewModel
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import com.yuyakaido.android.cardstackview.Duration
import com.yuyakaido.android.cardstackview.StackFrom
import com.yuyakaido.android.cardstackview.SwipeAnimationSetting
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SwipeFragment : Fragment() {

    private var _binding: FragmentSwipeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SwipeViewModel>()
    private val args: SwipeFragmentArgs by navArgs()

    private lateinit var manager: CardStackLayoutManager
    private val cardAdapter by lazy { CardStackAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        _binding = FragmentSwipeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLocationSheet()
        viewModel.getPersonsCards(args.userId)
        setupCardStack()
        setupButtons()
    }

    private fun setupCardStack() {
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

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.cards.collect { cards ->
                        cardAdapter.submitList(cards)
                    }
                }
                launch {
                    viewModel.matchEvent.collect { matchEvent ->
                        if (matchEvent) Toast.makeText(context, "Совпадение!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

    private fun swipeCard(direction: Direction) {
        val setting = SwipeAnimationSetting.Builder()
            .setDirection(direction)
            .setDuration(Duration.Normal.duration)
            .build()
        manager.setSwipeAnimationSetting(setting)
        binding.cardStackView.swipe()
    }

    private fun setupButtons() {
        with(binding) {
            btnLike.setOnClickListener { swipeCard(Direction.Right) }
            btnDislike.setOnClickListener { swipeCard(Direction.Left) }
            btnRefresh.setOnClickListener { cardStackView.rewind() }
        }
    }


    private val cardStackListener = object : CardStackListener {
        override fun onCardSwiped(direction: Direction?) {
            val swipedPosition = manager.topPosition - 1

            if (swipedPosition >= 0 && swipedPosition < cardAdapter.itemCount) {
                val targetUser = cardAdapter.currentList[swipedPosition]

                when (direction) {
                    Direction.Right -> viewModel.swipeUser(args.userId, targetUser.id, true)
                    Direction.Left -> viewModel.swipeUser(args.userId, targetUser.id, false)
                    else -> {}
                }
            }
        }

        override fun onCardRewound() {}
        override fun onCardDragging(direction: Direction?, ratio: Float) {}
        override fun onCardAppeared(view: View?, position: Int) {}
        override fun onCardDisappeared(view: View?, position: Int) {}
        override fun onCardCanceled() {}
    }

    private fun showLocationSheet() {
        val locationSheet = LocationFragment()
        locationSheet.show(childFragmentManager, "LocationBottomSheet")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}