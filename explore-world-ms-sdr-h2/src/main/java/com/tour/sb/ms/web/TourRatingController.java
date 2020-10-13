/**
 * 
 */
package com.tour.sb.ms.web;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tour.sb.ms.domain.Tour;
import com.tour.sb.ms.domain.TourRating;
import com.tour.sb.ms.domain.TourRatingPK;
import com.tour.sb.ms.repo.TourRatingRepository;
import com.tour.sb.ms.repo.TourRepository;

/**
 * Tour Rating Controller
 * 
 * @author amit
 *
 */

@RestController
@RequestMapping(path = "/tours/{tourId}/ratings")
public class TourRatingController {

	TourRatingRepository tourRatingRepository;
	TourRepository tourRepository;

	@Autowired
	public TourRatingController(TourRatingRepository tourRatingRepository, TourRepository tourRepository) {
		this.tourRatingRepository = tourRatingRepository;
		this.tourRepository = tourRepository;
	}

	protected TourRatingController() {

	}

	/**
	 * Create a Tour Rating.
	 *
	 * @param tourId    tour identifier
	 * @param ratingDto rating data transfer object
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public void createTourRating(@PathVariable(value = "tourId") int tourId,
			@RequestBody @Validated RatingDto ratingDto) {

		Tour tour = verifyTour(tourId);
		tourRatingRepository.save(new TourRating(new TourRatingPK(tour, ratingDto.getCustomerId()),
				ratingDto.getScore(), ratingDto.getComment()));
	}

	/**
	 * Lookup a the Ratings for a tour.
	 *
	 * @param tourId Tour Identifier
	 * @return All Tour Ratings as RatingDto's
	 */
	@GetMapping
	public List<RatingDto> getAllRatingsForTour(@PathVariable(value = "tourId") int tourId) {

		verifyTour(tourId);
		return tourRatingRepository.findByPkTourId(tourId).stream().map(RatingDto::new).collect(Collectors.toList());
	}

	/**
	 * Calculate the average Score of a Tour.
	 *
	 * @param tourId tour identifier
	 * @return Tuple of "average" and the average value.
	 */
	@GetMapping(path = "/average")
	public Map<String, Double> getAverage(@PathVariable(value = "tourId") int tourId) {

		verifyTour(tourId);

		return Map.of("average", tourRatingRepository.findByPkTourId(tourId).stream().mapToInt(TourRating::getScore)
				.average().orElseThrow(() -> new NoSuchElementException("Tour has no Ratings")));
	}

	/**
	 * Update score and comment of a Tour Rating
	 *
	 * @param tourId    tour identifier
	 * @param ratingDto rating Data Transfer Object
	 * @return The modified Rating DTO.
	 */
	@PutMapping
	public RatingDto updateWithPut(@PathVariable(value = "tourId") int tourId,
			@RequestBody @Validated RatingDto ratingDto) {

		TourRating rating = verifyTourRating(tourId, ratingDto.getCustomerId());
		rating.setScore(ratingDto.getScore());
		rating.setComment(ratingDto.getComment());

		return new RatingDto(tourRatingRepository.save(rating));

	}

	/**
	 * Update score or comment of a Tour Rating
	 *
	 * @param tourId    tour identifier
	 * @param ratingDto rating Data Transfer Object
	 * @return The modified Rating DTO.
	 */
	@PatchMapping
	public RatingDto updateWithPatch(@PathVariable(value = "tourId") int tourId,
			@RequestBody @Validated RatingDto ratingDto) {

		TourRating rating = verifyTourRating(tourId, ratingDto.getCustomerId());
		if (ratingDto.getScore() != null) {
			rating.setScore(ratingDto.getScore());
		}
		if (ratingDto.getComment() != null) {
			rating.setComment(ratingDto.getComment());
		}
		return new RatingDto(tourRatingRepository.save(rating));
	}

    /**
     * Delete a Rating of a tour made by a customer
     *
     * @param tourId tour identifier
     * @param customerId customer identifier
     */
	@DeleteMapping(path = "/{customerId}")
	public void delete(@PathVariable(value = "tourId") int tourId, 
			@PathVariable(value = "customerId") int customerId) {
		
		TourRating rating = verifyTourRating(tourId, customerId);
		tourRatingRepository.delete(rating);
	}
	
	/**
	 * @param tourId
	 * @param customerId
	 * @return
	 */
	private TourRating verifyTourRating(int tourId, Integer customerId) {

		return tourRatingRepository.findByPkTourIdAndPkCustomerId(tourId, customerId)
				.orElseThrow(() -> new NoSuchElementException(
						"Tour-Rating pair for request(" + tourId + " for customer " + customerId));
	}

	/**
	 * Verify and return the Tour given a tourId.
	 *
	 * @param tourId tour identifier
	 * @return the found Tour
	 * @throws NoSuchElementException if no Tour found.
	 */
	private Tour verifyTour(int tourId) {
		// TODO Auto-generated method stub
		return tourRepository.findById(tourId)
				.orElseThrow(() -> new NoSuchElementException("Tour does not exist :" + tourId));
	}

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(NoSuchElementException.class)
	public String return400(NoSuchElementException ex) {
		return ex.getMessage();
	}
}