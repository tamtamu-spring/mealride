package hu.student.projlab.mealride.meal;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealRepository extends JpaRepository<Meal, Long> {

    //List<Meal> findAllByRestaurantId(Long restId);

}
