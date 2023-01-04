package edu.javacourse.city.dao;

import edu.javacourse.city.dao.domain.PersonRequest;
import edu.javacourse.city.dao.domain.PersonResponse;
import edu.javacourse.city.exception.PersonCheckException;

import java.sql.*;


public class PersonCheckDao {
    public static final String  SQL_REQUEST="select temporal,p.sur_name from cr_address_person op\n" +
            "            inner join cr_person p on p.person_id=op.person_id\n" +
            "            inner join cr_address a on a.address_id=op.address_id\n" +
            "            where CURRENT_DATE>=op.start_date and (CURRENT_DATE<=op.end_date or op.end_date is null) and p.sur_name=?  and p.given_name=? and patronymic=? and p.date_of_birth=?\n" +
            "            and a.street_code=? and a.building=? ";
private ConnectionBuilder connectionBuilder;

    public void setConnectionBuilder(ConnectionBuilder connectionBuilder) {
        this.connectionBuilder = connectionBuilder;
    }
    private Connection getConnection() throws SQLException {return connectionBuilder.getConnection();
    }
    public PersonResponse checkPerson (PersonRequest request) throws PersonCheckException{

PersonResponse response=new PersonResponse();
String s=SQL_REQUEST;
if(request.getExtension() != null){s +="and extension=? ";}
else {s +="and extension is null ";}
if(request.getApartment() != null){s +="and a.apartment=?";}
else {s +="and a.apartment is null";}
try (
    Connection con = getConnection();PreparedStatement stmt=con.prepareStatement(s))
{int count=1;
    stmt.setString(count++,request.getSurName());
    stmt.setString(count++,request.getGivenName());
    stmt.setString(count++,request.getPatronymic());
    stmt.setDate(count++,java.sql.Date.valueOf(request.getDateOfBirth()));
    stmt.setInt(count++,request.getStreetCode());
    stmt.setString(count++,request.getBuilding());
    if  (request.getExtension() != null) {
    stmt.setString(count++,request.getExtension());}
    if (request.getApartment() != null) {
    stmt.setString(count++, request.getApartment());}
    ResultSet rs=stmt.executeQuery();
    if(rs.next())
    {response.setRegistered(true);
    response.setTemporal(rs.getBoolean("temporal"));
}}
catch (SQLException ex){throw new PersonCheckException();
    }
    return response;

}


    }
