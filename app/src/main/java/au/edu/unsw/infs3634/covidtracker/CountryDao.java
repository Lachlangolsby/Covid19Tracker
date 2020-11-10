package au.edu.unsw.infs3634.covidtracker;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CountryDao {

    @Query ("SELECT * FROM Country")
    List<Country> getCountries();

    @Query ("SELECT * FROM Country Where CountryCode == :countryCode")
    Country getCountry(String countryCode);

    @Insert
    void insertCountries(Country... countries);

    @Delete
    void deleteAll (Country...countries);

}
