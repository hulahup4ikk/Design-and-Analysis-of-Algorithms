## 1. Architecture Notes
- **QuickSort (Randomized, bounded stack):**
  - Recurses only on the smaller side and iterates over the larger side, ensuring recursion depth `O(log n)` instead of `O(n)`.
  - Randomized pivot prevents adversarial patterns from causing quadratic behavior.
- **MergeSort:**
  - Always divides input in half; recursion depth fixed at `log n`.
  - Uses auxiliary buffer allocated once; memory usage `O(n)`.
- **DeterministicSelect (Median-of-Medians):**
  - Partitions array with three-way split; recursion goes only into one side.
  - Pivot is guaranteed "good": at least 30%–70% split, so recursion depth is `O(log n)`.
- **Closest Pair of Points:**
  - Sorts once by x and maintains y-order during recursion.
  - Only `O(n)` work per recursion level; recursion depth is `O(log n)`.

---

## 2. Recurrence Analyses

- **QuickSort (Randomized):**  
  Recurrence:  
  `T(n) = T(αn) + T((1–α)n) + Θ(n)` with α random in [0,1].  
  Expected depth ≈ `Θ(log n)`; expected work `Θ(n log n)` (by linearity of expectation).  
  Randomization avoids worst-case `Θ(n^2)`.  

- **MergeSort:**  
  Recurrence:  
  `T(n) = 2T(n/2) + Θ(n)`.  
  By Master Theorem: `Θ(n log n)`.  
  Depth exactly `log₂ n`, stack cost `O(log n)`.  

- **DeterministicSelect (Median-of-Medians):**  
  Recurrence:  
  `T(n) ≤ T(n/5) + T(7n/10) + Θ(n)`.  
  By Akra–Bazzi: `Θ(n)`.  
  Guarantees linear time regardless of input distribution.  

- **Closest Pair (Divide & Conquer):**  
  Recurrence:  
  `T(n) = 2T(n/2) + Θ(n)`.  
  Master Theorem gives `Θ(n log n)`.  
  The strip check is linear, bounded by 7 comparisons per point.  

---

## 3. Experimental Results and Plots

- **QuickSort:**  
  Depth measured ≈ 7–9 for n=20000 (theoretical bound ≈ 48).  
  Time grows close to `n log n`, constant factors small.  

- **MergeSort:**  
  All test cases sorted correctly, depth stable at log₂ n.  
  Slightly slower than QuickSort due to buffer writes, but more predictable.  

- **DeterministicSelect:**  
  Passed 100/100 random trials.  
  Time grows linearly, but higher constant factor (extra median sorting).  

- **Closest Pair:**  
  Matches brute force for n ≤ 2000;  
  Fast version scales to n=100000 in ~0.25s.  
  Constant factor noticeable from sorting and strip merging.  


---

## 4. Discussion of Constant-Factor Effects
- **Cache:** MergeSort benefits from sequential memory access.  
- **Garbage Collection (GC):** negligible for primitive arrays.  
- **Randomization overhead:** QuickSort pays ~constant cost per pivot, but wins on partition balance.  
- **Median-of-Medians:** linear but larger constant due to sorting groups of 5.  

---

## 5. Summary
- **Theory vs Practice:**  
  - QuickSort: empirical depth far smaller than worst-case bound; runtime aligns with `Θ(n log n)`.  
  - MergeSort: matches theory exactly; slightly slower constants than QuickSort.  
  - DeterministicSelect: matches `Θ(n)`, constants noticeable but acceptable.  
  - Closest Pair: matches `Θ(n log n)` with ~7 comparisons per point in strip.  
- **Alignment:** Strong agreement between theoretical recurrence analyses and measurements.  
- **Mismatch:** Only constant factors differ (QuickSort often faster in practice despite worse worst-case bound).  

---
