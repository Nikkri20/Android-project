package com.example.android_project.data.repository

import com.example.android_project.R
import com.example.android_project.data.model.Character

class CharacterRepository {
    fun getCharacters(): List<Character> {
        return listOf(
            Character(
                id = "1",
                name = "Eric Theodore Cartman",
                gender = "Male",
                age = 10,
                hairColor = "Brown",
                occupation = "Student",
                grade = "4th Grade",
                aliases = "A.W.E.S.O.M.-O 4000, Dawg the Hall Monitor, Eric Cartman, The Coon, The Grand Wizard King, Mitch Conner, Mr. Cartmanez, Fatass",
                religion = "Roman Catholic, Blaintologist (formerly) Judaism (formerly)",
                mother = "Liane Cartman",
                father = "Jack Tenorman",
                halfBrother = "Scott Tenorman",
                grandmother = "Mabel Cartman",
                grandfather = "Harold Cartman",
                aunt = "Lisa Cartman",
                uncle = "Uncle Stinky",
                cousin = "Alexandra Cartman",
                voicedBy = "Trey Parker",
                firstAppearance = "The Spirit of Christmas (Jesus vs. Frosty)",
                imageResId = R.drawable.eric_cartman
            ),
            Character(
                id = "2",
                name = "Stanley \"Stan\" Marsh",
                gender = "Male",
                age = 10,
                hairColor = "Black",
                occupation = "Student",
                grade = "4th Grade",
                aliases = "Ranger Stan Marshwalker, Satan (Stan Marsh), Toolshed, Stan Darsh, The Boy in the Red Poofball Hat",
                religion = "Roman Catholic; Temporarily: Atheist, Mormon, Scientologist and Blaintologist",
                father = "Randy Marsh",
                halfBrother = "None",
                mother = "Sharon Marsh",
                grandfather = "Marvin Marsh",
                grandmother = "Grandma Marsh",
                aunt = null,
                uncle = "Jimbo Kern",
                cousin = null,
                voicedBy = "Trey Parker",
                firstAppearance = "The Spirit of Christmas (Jesus vs. Frosty)",
                imageResId = R.drawable.stan_marsh
            ),
            Character(
                id = "3",
                name = "Kenneth \"Kenny\" McCormick",
                gender = "Male",
                age = 9,
                hairColor = "Blond",
                occupation = "Student",
                grade = "4th Grade",
                aliases = "Mysterion, El Pollo Loco, Princess Kenny, Dennis",
                religion = "Roman Catholic, Temporary: Blaintologist",
                father = "Stuart McCormick",
                mother = "Mrs. McCormick",
                halfBrother = null,
                grandmother = null,
                grandfather = "Grandpa McCormick",
                aunt = null,
                uncle = null,
                cousin = null,
                voicedBy = "Matt Stone (muffled), Eric Stough (unmuffled)",
                firstAppearance = "The Spirit of Christmas (Jesus vs. Frosty)",
                imageResId = R.drawable.kenny_mccormick
            ),
            Character(
                id = "4",
                name = "Kyle Broflovski",
                gender = "Male",
                age = 10,
                hairColor = "Ginger",
                occupation = "Student",
                grade = "4th Grade",
                aliases = "High Jew Elf King, Paladin Kyle, The Human Kite, Kyley-B, Daywalker, Kyle of the DeVry Institute, Fart Boy, Khal, Kyle 2",
                religion = "Judaism, Blaintology (temporarily), Roman Catholic (temporarily)",
                father = "Gerald Broflovski",
                mother = "Sheila Broflovski",
                halfBrother = null,
                grandmother = "Cleo Broflovski",
                grandfather = null,
                aunt = null,
                uncle = "Murrey Broflovski",
                cousin = "Kyle Schwartz",
                voicedBy = "Matt Stone",
                firstAppearance = "The Spirit of Christmas (Jesus vs. Frosty)",
                imageResId = R.drawable.kyle_broflovski
            )
        )
    }
}
