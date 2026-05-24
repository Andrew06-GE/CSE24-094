package com.gabsstudentstay.gabsstudentstay.utils

import com.gabsstudentstay.gabsstudentstay.data.db.model.Listing
import com.gabsstudentstay.gabsstudentstay.data.db.model.User
import com.gabsstudentstay.gabsstudentstay.data.db.repository.ListingRepository
import com.gabsstudentstay.gabsstudentstay.data.db.repository.UserRepository

object DataSeeder {

    suspend fun seed(userRepo: UserRepository, listingRepo: ListingRepository) {
        userRepo.insertAll(generateStudents())
        userRepo.insertAll(generateProviders())
        listingRepo.insertAll(generateListings())
    }

    // ── 50 STUDENTS ───────────────────────────────────────────────────────────
    private fun generateStudents(): List<User> {
        val firstNames = listOf(
            "Tebogo","Kefilwe","Mpho","Boitumelo","Thabo","Lesedi","Onkabetse",
            "Kagiso","Goitseone","Dineo","Olorato","Tlhogi","Neo","Refilwe",
            "Tshepo","Gaone","Ogomoditse","Botshelo","Moagi","Phenyo",
            "Gosego","Motlhoki","Kenosi","Thato","Bogosi","Masego","Peo",
            "Kabo","Tumelo","Lorato","Modisa","Kopano","Obakeng","Sebata",
            "Bongani","Naledi","Sethebe","Mmamoloki","Tiisetso","Goabaone",
            "Mmoloki","Matlho","Bogolo","Tsholofelo","Kesego","Keabetswe",
            "Oreeditse","Onalerona","Gaborone","Tshiamo"
        )
        val surnames = listOf(
            "Mokoena","Sithole","Dlamini","Molefe","Kgosi","Tshweu","Ramotswe",
            "Gabaake","Makwela","Modise","Nthaga","Sebele","Osei","Letshwenyo",
            "Phiri","Tlotleng","Ntsimane","Segwe","Mothibi","Kgang"
        )

        return (1..50).map { i ->
            val first   = firstNames[i - 1]
            val surname = surnames[(i - 1) % surnames.size]
            User(
                name      = "$first $surname",
                email     = "${first.lowercase()}.${surname.lowercase()}$i@student.ub.bw",
                password  = "password$i",
                role      = "student",
                studentId = "UB${(2000000 + i)}",
                phone     = "7${(1000000..9999999).random()}"
            )
        }
    }

    // ── PROVIDERS (landlords) ─────────────────────────────────────────────────
    private fun generateProviders(): List<User> = listOf(
        User(name="Joseph Molefe",  email="joseph@provider.bw", password="landlord1", role="provider", studentId="", phone="71234567"),
        User(name="Grace Setshedi", email="grace@provider.bw",  password="landlord2", role="provider", studentId="", phone="72345678"),
        User(name="Obed Kgari",     email="obed@provider.bw",   password="landlord3", role="provider", studentId="", phone="73456789"),
        User(name="Mpho Tiro",      email="mpho@provider.bw",   password="landlord4", role="provider", studentId="", phone="74567890"),
        User(name="Ruth Sechele",   email="ruth@provider.bw",   password="landlord5", role="provider", studentId="", phone="75678901")
    )

    // ── 50 LISTINGS ───────────────────────────────────────────────────────────
    private fun generateListings(): List<Listing> {
        val locations = listOf(
            "Gaborone West","Phase 2","Block 6","Block 7","Block 8",
            "Tlokweng","Old Naledi","Broadhurst","Mogoditshane","Phakalane",
            "Gabane","Lentsweletau","Mmopane","Thamaga","Molepolole"
        )
        val types = listOf("Single Room","Bachelor Flat","Shared","Cottage")
        val amenityOptions = listOf(
            "WiFi,Water,Electricity",
            "Water,Electricity,Parking",
            "WiFi,Water,Electricity,Parking",
            "Water,Electricity",
            "WiFi,Water,Electricity,Parking,Laundry",
            "Water,Electricity,Security"
        )
        val descriptions = listOf(
            "Cozy and well-lit room near campus with reliable water and power supply.",
            "Spacious bachelor flat ideal for one student, close to public transport.",
            "Quiet shared accommodation in a secure yard with 24hr security.",
            "Self-contained cottage with private entrance and kitchenette.",
            "Affordable single room, landlord on-site. Bills included.",
            "Modern flat with fibre WiFi and off-street parking.",
            "Newly painted room with tiled floors and built-in wardrobe.",
            "Furnished room with a bed, desk, and wardrobe. DSTV connection available.",
            "Clean, airy room in a family yard. Suitable for female students.",
            "Recently renovated cottage with indoor bathroom and private gate."
        )

        val prices = listOf(
            800.0, 900.0, 1000.0, 1100.0, 1200.0, 1300.0, 1400.0, 1500.0,
            1600.0, 1800.0, 2000.0, 2200.0, 2500.0, 2800.0, 3000.0,
            850.0, 950.0, 1050.0, 1150.0, 1250.0
        )

        // Approximate lat/lng offsets around Gaborone centre
        val latBase  = -24.6541
        val lngBase  =  25.9087
        val offsets  = (-10..10).map { it * 0.005 }

        return (1..50).map { i ->
            val providerId = ((i - 1) % 5) + 51  // Maps to provider user IDs (inserted after 50 students)
            Listing(
                providerId      = providerId,
                title           = "${types[(i - 1) % types.size]} #$i – ${locations[(i - 1) % locations.size]}",
                price           = prices[(i - 1) % prices.size],
                location        = locations[(i - 1) % locations.size],
                type            = types[(i - 1) % types.size],
                amenities       = amenityOptions[(i - 1) % amenityOptions.size],
                availabilityDate = availabilityDate(i),
                depositAmount   = prices[(i - 1) % prices.size],  // 1 month deposit
                description     = descriptions[(i - 1) % descriptions.size],
                latitude        = latBase + offsets[(i - 1) % offsets.size],
                longitude       = lngBase + offsets[(i - 1) % offsets.size],
                isReserved      = false
            )
        }
    }

    private fun availabilityDate(index: Int): String {
        // Spread availability dates across May–August 2026
        val month = when ((index - 1) % 4) {
            0    -> "2026-05-01"
            1    -> "2026-06-01"
            2    -> "2026-07-01"
            else -> "2026-08-01"
        }
        return month
    }
}