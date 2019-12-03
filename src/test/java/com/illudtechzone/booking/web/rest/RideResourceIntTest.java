package com.illudtechzone.booking.web.rest;

import com.illudtechzone.booking.BookingApp;

import com.illudtechzone.booking.domain.Ride;
import com.illudtechzone.booking.repository.RideRepository;
import com.illudtechzone.booking.repository.search.RideSearchRepository;
import com.illudtechzone.booking.service.RideService;
import com.illudtechzone.booking.service.dto.RideDTO;
import com.illudtechzone.booking.service.mapper.RideMapper;
import com.illudtechzone.booking.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;


import static com.illudtechzone.booking.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.illudtechzone.booking.domain.enumeration.RequestStatus;
import com.illudtechzone.booking.domain.enumeration.RideStatus;
/**
 * Test class for the RideResource REST controller.
 *
 * @see RideResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookingApp.class)
public class RideResourceIntTest {

    private static final Long DEFAULT_DISTANCE = 1L;
    private static final Long UPDATED_DISTANCE = 2L;

    private static final String DEFAULT_PICKUP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_PICKUP_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PICKUP_PLACE_ID = "AAAAAAAAAA";
    private static final String UPDATED_PICKUP_PLACE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION_PLACE_ID = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION_PLACE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PICKUP_GEOPOINT = "AAAAAAAAAA";
    private static final String UPDATED_PICKUP_GEOPOINT = "BBBBBBBBBB";

    private static final String DEFAULT_DESTINATION_GEOPOINT = "AAAAAAAAAA";
    private static final String UPDATED_DESTINATION_GEOPOINT = "BBBBBBBBBB";

    private static final Long DEFAULT_CUSTOMER_ID = 1L;
    private static final Long UPDATED_CUSTOMER_ID = 2L;

    private static final RequestStatus DEFAULT_REQUESTED_STATUS = RequestStatus.REQUEST;
    private static final RequestStatus UPDATED_REQUESTED_STATUS = RequestStatus.CONFIRM;

    private static final RideStatus DEFAULT_ACCEPTED_STATUS = RideStatus.START;
    private static final RideStatus UPDATED_ACCEPTED_STATUS = RideStatus.COMPLETE;

    private static final Long DEFAULT_DRIVER_ID = 1L;
    private static final Long UPDATED_DRIVER_ID = 2L;

    private static final Double DEFAULT_COST = 1D;
    private static final Double UPDATED_COST = 2D;

    private static final Instant DEFAULT_CREATED_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_TRACKING_ID = "AAAAAAAAAA";
    private static final String UPDATED_TRACKING_ID = "BBBBBBBBBB";

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private RideMapper rideMapper;

    @Autowired
    private RideService rideService;

    /**
     * This repository is mocked in the com.illudtechzone.booking.repository.search test package.
     *
     * @see com.illudtechzone.booking.repository.search.RideSearchRepositoryMockConfiguration
     */
    @Autowired
    private RideSearchRepository mockRideSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restRideMockMvc;

    private Ride ride;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RideResource rideResource = new RideResource(rideService);
        this.restRideMockMvc = MockMvcBuilders.standaloneSetup(rideResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ride createEntity(EntityManager em) {
        Ride ride = new Ride()
            .distance(DEFAULT_DISTANCE)
            .pickupAddress(DEFAULT_PICKUP_ADDRESS)
            .pickupPlaceId(DEFAULT_PICKUP_PLACE_ID)
            .destinationPlaceId(DEFAULT_DESTINATION_PLACE_ID)
            .destinationAddress(DEFAULT_DESTINATION_ADDRESS)
            .pickupGeopoint(DEFAULT_PICKUP_GEOPOINT)
            .destinationGeopoint(DEFAULT_DESTINATION_GEOPOINT)
            .customerId(DEFAULT_CUSTOMER_ID)
            .requestedStatus(DEFAULT_REQUESTED_STATUS)
            .acceptedStatus(DEFAULT_ACCEPTED_STATUS)
            .driverId(DEFAULT_DRIVER_ID)
            .cost(DEFAULT_COST)
            .createdTime(DEFAULT_CREATED_TIME)
            .trackingId(DEFAULT_TRACKING_ID);
        return ride;
    }

    @Before
    public void initTest() {
        ride = createEntity(em);
    }

    @Test
    @Transactional
    public void createRide() throws Exception {
        int databaseSizeBeforeCreate = rideRepository.findAll().size();

        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);
        restRideMockMvc.perform(post("/api/rides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rideDTO)))
            .andExpect(status().isCreated());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeCreate + 1);
        Ride testRide = rideList.get(rideList.size() - 1);
        assertThat(testRide.getDistance()).isEqualTo(DEFAULT_DISTANCE);
        assertThat(testRide.getPickupAddress()).isEqualTo(DEFAULT_PICKUP_ADDRESS);
        assertThat(testRide.getPickupPlaceId()).isEqualTo(DEFAULT_PICKUP_PLACE_ID);
        assertThat(testRide.getDestinationPlaceId()).isEqualTo(DEFAULT_DESTINATION_PLACE_ID);
        assertThat(testRide.getDestinationAddress()).isEqualTo(DEFAULT_DESTINATION_ADDRESS);
        assertThat(testRide.getPickupGeopoint()).isEqualTo(DEFAULT_PICKUP_GEOPOINT);
        assertThat(testRide.getDestinationGeopoint()).isEqualTo(DEFAULT_DESTINATION_GEOPOINT);
        assertThat(testRide.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testRide.getRequestedStatus()).isEqualTo(DEFAULT_REQUESTED_STATUS);
        assertThat(testRide.getAcceptedStatus()).isEqualTo(DEFAULT_ACCEPTED_STATUS);
        assertThat(testRide.getDriverId()).isEqualTo(DEFAULT_DRIVER_ID);
        assertThat(testRide.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testRide.getCreatedTime()).isEqualTo(DEFAULT_CREATED_TIME);
        assertThat(testRide.getTrackingId()).isEqualTo(DEFAULT_TRACKING_ID);

        // Validate the Ride in Elasticsearch
        verify(mockRideSearchRepository, times(1)).save(testRide);
    }

    @Test
    @Transactional
    public void createRideWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rideRepository.findAll().size();

        // Create the Ride with an existing ID
        ride.setId(1L);
        RideDTO rideDTO = rideMapper.toDto(ride);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRideMockMvc.perform(post("/api/rides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rideDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeCreate);

        // Validate the Ride in Elasticsearch
        verify(mockRideSearchRepository, times(0)).save(ride);
    }

    @Test
    @Transactional
    public void getAllRides() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get all the rideList
        restRideMockMvc.perform(get("/api/rides?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ride.getId().intValue())))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE.intValue())))
            .andExpect(jsonPath("$.[*].pickupAddress").value(hasItem(DEFAULT_PICKUP_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].pickupPlaceId").value(hasItem(DEFAULT_PICKUP_PLACE_ID.toString())))
            .andExpect(jsonPath("$.[*].destinationPlaceId").value(hasItem(DEFAULT_DESTINATION_PLACE_ID.toString())))
            .andExpect(jsonPath("$.[*].destinationAddress").value(hasItem(DEFAULT_DESTINATION_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].pickupGeopoint").value(hasItem(DEFAULT_PICKUP_GEOPOINT.toString())))
            .andExpect(jsonPath("$.[*].destinationGeopoint").value(hasItem(DEFAULT_DESTINATION_GEOPOINT.toString())))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].requestedStatus").value(hasItem(DEFAULT_REQUESTED_STATUS.toString())))
            .andExpect(jsonPath("$.[*].acceptedStatus").value(hasItem(DEFAULT_ACCEPTED_STATUS.toString())))
            .andExpect(jsonPath("$.[*].driverId").value(hasItem(DEFAULT_DRIVER_ID.intValue())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].trackingId").value(hasItem(DEFAULT_TRACKING_ID.toString())));
    }
    
    @Test
    @Transactional
    public void getRide() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        // Get the ride
        restRideMockMvc.perform(get("/api/rides/{id}", ride.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ride.getId().intValue()))
            .andExpect(jsonPath("$.distance").value(DEFAULT_DISTANCE.intValue()))
            .andExpect(jsonPath("$.pickupAddress").value(DEFAULT_PICKUP_ADDRESS.toString()))
            .andExpect(jsonPath("$.pickupPlaceId").value(DEFAULT_PICKUP_PLACE_ID.toString()))
            .andExpect(jsonPath("$.destinationPlaceId").value(DEFAULT_DESTINATION_PLACE_ID.toString()))
            .andExpect(jsonPath("$.destinationAddress").value(DEFAULT_DESTINATION_ADDRESS.toString()))
            .andExpect(jsonPath("$.pickupGeopoint").value(DEFAULT_PICKUP_GEOPOINT.toString()))
            .andExpect(jsonPath("$.destinationGeopoint").value(DEFAULT_DESTINATION_GEOPOINT.toString()))
            .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID.intValue()))
            .andExpect(jsonPath("$.requestedStatus").value(DEFAULT_REQUESTED_STATUS.toString()))
            .andExpect(jsonPath("$.acceptedStatus").value(DEFAULT_ACCEPTED_STATUS.toString()))
            .andExpect(jsonPath("$.driverId").value(DEFAULT_DRIVER_ID.intValue()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.doubleValue()))
            .andExpect(jsonPath("$.createdTime").value(DEFAULT_CREATED_TIME.toString()))
            .andExpect(jsonPath("$.trackingId").value(DEFAULT_TRACKING_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRide() throws Exception {
        // Get the ride
        restRideMockMvc.perform(get("/api/rides/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRide() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        int databaseSizeBeforeUpdate = rideRepository.findAll().size();

        // Update the ride
        Ride updatedRide = rideRepository.findById(ride.getId()).get();
        // Disconnect from session so that the updates on updatedRide are not directly saved in db
        em.detach(updatedRide);
        updatedRide
            .distance(UPDATED_DISTANCE)
            .pickupAddress(UPDATED_PICKUP_ADDRESS)
            .pickupPlaceId(UPDATED_PICKUP_PLACE_ID)
            .destinationPlaceId(UPDATED_DESTINATION_PLACE_ID)
            .destinationAddress(UPDATED_DESTINATION_ADDRESS)
            .pickupGeopoint(UPDATED_PICKUP_GEOPOINT)
            .destinationGeopoint(UPDATED_DESTINATION_GEOPOINT)
            .customerId(UPDATED_CUSTOMER_ID)
            .requestedStatus(UPDATED_REQUESTED_STATUS)
            .acceptedStatus(UPDATED_ACCEPTED_STATUS)
            .driverId(UPDATED_DRIVER_ID)
            .cost(UPDATED_COST)
            .createdTime(UPDATED_CREATED_TIME)
            .trackingId(UPDATED_TRACKING_ID);
        RideDTO rideDTO = rideMapper.toDto(updatedRide);

        restRideMockMvc.perform(put("/api/rides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rideDTO)))
            .andExpect(status().isOk());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeUpdate);
        Ride testRide = rideList.get(rideList.size() - 1);
        assertThat(testRide.getDistance()).isEqualTo(UPDATED_DISTANCE);
        assertThat(testRide.getPickupAddress()).isEqualTo(UPDATED_PICKUP_ADDRESS);
        assertThat(testRide.getPickupPlaceId()).isEqualTo(UPDATED_PICKUP_PLACE_ID);
        assertThat(testRide.getDestinationPlaceId()).isEqualTo(UPDATED_DESTINATION_PLACE_ID);
        assertThat(testRide.getDestinationAddress()).isEqualTo(UPDATED_DESTINATION_ADDRESS);
        assertThat(testRide.getPickupGeopoint()).isEqualTo(UPDATED_PICKUP_GEOPOINT);
        assertThat(testRide.getDestinationGeopoint()).isEqualTo(UPDATED_DESTINATION_GEOPOINT);
        assertThat(testRide.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testRide.getRequestedStatus()).isEqualTo(UPDATED_REQUESTED_STATUS);
        assertThat(testRide.getAcceptedStatus()).isEqualTo(UPDATED_ACCEPTED_STATUS);
        assertThat(testRide.getDriverId()).isEqualTo(UPDATED_DRIVER_ID);
        assertThat(testRide.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testRide.getCreatedTime()).isEqualTo(UPDATED_CREATED_TIME);
        assertThat(testRide.getTrackingId()).isEqualTo(UPDATED_TRACKING_ID);

        // Validate the Ride in Elasticsearch
        verify(mockRideSearchRepository, times(1)).save(testRide);
    }

    @Test
    @Transactional
    public void updateNonExistingRide() throws Exception {
        int databaseSizeBeforeUpdate = rideRepository.findAll().size();

        // Create the Ride
        RideDTO rideDTO = rideMapper.toDto(ride);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRideMockMvc.perform(put("/api/rides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rideDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Ride in the database
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Ride in Elasticsearch
        verify(mockRideSearchRepository, times(0)).save(ride);
    }

    @Test
    @Transactional
    public void deleteRide() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);

        int databaseSizeBeforeDelete = rideRepository.findAll().size();

        // Delete the ride
        restRideMockMvc.perform(delete("/api/rides/{id}", ride.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Ride> rideList = rideRepository.findAll();
        assertThat(rideList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Ride in Elasticsearch
        verify(mockRideSearchRepository, times(1)).deleteById(ride.getId());
    }

    @Test
    @Transactional
    public void searchRide() throws Exception {
        // Initialize the database
        rideRepository.saveAndFlush(ride);
        when(mockRideSearchRepository.search(queryStringQuery("id:" + ride.getId()), PageRequest.of(0, 20)))
            .thenReturn(new PageImpl<>(Collections.singletonList(ride), PageRequest.of(0, 1), 1));
        // Search the ride
        restRideMockMvc.perform(get("/api/_search/rides?query=id:" + ride.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ride.getId().intValue())))
            .andExpect(jsonPath("$.[*].distance").value(hasItem(DEFAULT_DISTANCE.intValue())))
            .andExpect(jsonPath("$.[*].pickupAddress").value(hasItem(DEFAULT_PICKUP_ADDRESS)))
            .andExpect(jsonPath("$.[*].pickupPlaceId").value(hasItem(DEFAULT_PICKUP_PLACE_ID)))
            .andExpect(jsonPath("$.[*].destinationPlaceId").value(hasItem(DEFAULT_DESTINATION_PLACE_ID)))
            .andExpect(jsonPath("$.[*].destinationAddress").value(hasItem(DEFAULT_DESTINATION_ADDRESS)))
            .andExpect(jsonPath("$.[*].pickupGeopoint").value(hasItem(DEFAULT_PICKUP_GEOPOINT)))
            .andExpect(jsonPath("$.[*].destinationGeopoint").value(hasItem(DEFAULT_DESTINATION_GEOPOINT)))
            .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID.intValue())))
            .andExpect(jsonPath("$.[*].requestedStatus").value(hasItem(DEFAULT_REQUESTED_STATUS.toString())))
            .andExpect(jsonPath("$.[*].acceptedStatus").value(hasItem(DEFAULT_ACCEPTED_STATUS.toString())))
            .andExpect(jsonPath("$.[*].driverId").value(hasItem(DEFAULT_DRIVER_ID.intValue())))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.doubleValue())))
            .andExpect(jsonPath("$.[*].createdTime").value(hasItem(DEFAULT_CREATED_TIME.toString())))
            .andExpect(jsonPath("$.[*].trackingId").value(hasItem(DEFAULT_TRACKING_ID)));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ride.class);
        Ride ride1 = new Ride();
        ride1.setId(1L);
        Ride ride2 = new Ride();
        ride2.setId(ride1.getId());
        assertThat(ride1).isEqualTo(ride2);
        ride2.setId(2L);
        assertThat(ride1).isNotEqualTo(ride2);
        ride1.setId(null);
        assertThat(ride1).isNotEqualTo(ride2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RideDTO.class);
        RideDTO rideDTO1 = new RideDTO();
        rideDTO1.setId(1L);
        RideDTO rideDTO2 = new RideDTO();
        assertThat(rideDTO1).isNotEqualTo(rideDTO2);
        rideDTO2.setId(rideDTO1.getId());
        assertThat(rideDTO1).isEqualTo(rideDTO2);
        rideDTO2.setId(2L);
        assertThat(rideDTO1).isNotEqualTo(rideDTO2);
        rideDTO1.setId(null);
        assertThat(rideDTO1).isNotEqualTo(rideDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rideMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rideMapper.fromId(null)).isNull();
    }
}
