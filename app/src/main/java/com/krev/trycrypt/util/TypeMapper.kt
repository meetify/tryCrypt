package com.krev.trycrypt.util

import com.krev.trycrypt.R.drawable

object TypeMapper {
    fun drawable(types: List<String>): Int {
        types.forEach {
            return when (it.toLowerCase()) {
                "accounting" -> drawable.ic_account_balance
                "airport" -> drawable.ic_flight
                "amusement_park" -> drawable.ic_mood
                "art_gallery" -> drawable.ic_image
                "atm" -> drawable.ic_local_atm
                "bank" -> drawable.ic_local_atm
                "bar" -> drawable.ic_local_bar
                "bicycle_store" -> drawable.ic_directions_bike
                "book_store" -> drawable.ic_book
                "bus_station" -> drawable.ic_directions_bus
                "cafe" -> drawable.ic_local_cafe
                "campground" -> drawable.ic_terrain
                "car_dealer" -> drawable.ic_directions_car
                "car_rental" -> drawable.ic_directions_car
                "car_repair" -> drawable.ic_directions_car
                "car_wash" -> drawable.ic_directions_car
                "casino" -> drawable.ic_casino
                "cemetery" -> drawable.ic_cemetery
                "city_hall" -> drawable.ic_location_city
                "clothing_store" -> drawable.ic_polo_shirt
                "convenience_store" -> drawable.ic_shopping_cart
                "courthouse" -> drawable.ic_building_6
                "department_store" -> drawable.ic_shopping_cart
                "dentist" -> drawable.ic_health_heart_pulse
                "doctor" -> drawable.ic_health_heart_pulse
                "embassy" -> drawable.ic_building_6
                "electronics_store" -> drawable.ic_memory
                "finance" -> drawable.ic_attach_money
                "fire_station" -> drawable.ic_fire
                "florist" -> drawable.ic_local_florist
                "food" -> drawable.ic_local_pizza
                "funeral_home" -> drawable.ic_cemetery
                "furniture_store" -> drawable.ic_cabinet
                "gas_station" -> drawable.ic_local_gas_station
                "grocery_or_supermarket" -> drawable.ic_shopping_cart
                "gym" -> drawable.ic_fitness_center
                "hardware_store" -> drawable.ic_memory
                "health" -> drawable.ic_health_heart_pulse
                "home_goods_store" -> drawable.ic_local_grocery_store
                "hospital" -> drawable.ic_health_heart_pulse
                "jewelry_store" -> drawable.ic_diamond
                "lawyer" -> drawable.ic_building_6
                "library" -> drawable.ic_local_library
                "liquor_store" -> drawable.ic_local_bar
                "local_government_office" -> drawable.ic_building_6
                "locksmith" -> drawable.ic_hammer_anvil
                "lodging" -> drawable.ic_home
                "meal_delivery" -> drawable.ic_delivery
                "meal_takeaway" -> drawable.ic_delivery
                "movie_theater" -> drawable.ic_movie
                "movie_rental" -> drawable.ic_movie
                "moving_company" -> drawable.ic_local_shipping
                "museum" -> drawable.ic_temple_2
                "night_club" -> drawable.ic_local_bar
                "painter" -> drawable.ic_format_paint
                "park" -> drawable.ic_nature_people
                "parking" -> drawable.ic_local_parking
                "pet_store" -> drawable.ic_pets
                "pharmacy" -> drawable.ic_local_pharmacy
                "physiotherapist" -> drawable.ic_local_pharmacy
                "police" -> drawable.ic_police_officer_1
                "post_office" -> drawable.ic_local_post_office
                "real_estate_agency" -> drawable.ic_home
                "restaurant" -> drawable.ic_restaurant
                "school" -> drawable.ic_school
                "shoe_store" -> drawable.ic_shoe
                "shopping_mall" -> drawable.ic_local_mall
                "spa" -> drawable.ic_spa_lotus_flower
                "stadium" -> drawable.ic_stadium
                "storage" -> drawable.ic_storage
                "store" -> drawable.ic_local_grocery_store
                "subway_station" -> drawable.ic_directions_subway
                "taxi_stand" -> drawable.ic_local_taxi
                "train_station" -> drawable.ic_train
                "travel_agency" -> drawable.ic_flight
                "university" -> drawable.ic_school
                "veterinary_care" -> drawable.ic_pets
                "zoo" -> drawable.ic_pets
                else -> return@forEach
            }
        }
        return drawable.ic_place
    }
}