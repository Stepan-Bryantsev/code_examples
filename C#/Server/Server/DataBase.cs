using System;
using System.Collections.Generic;
using MySql.Data.MySqlClient;
using ServerClassesLibrary;

namespace Server
{
    class DatabaseConnector
    {
        const string connectionStr = "server=localhost;user=Stepa;database=notes_db;password=12980000";

        MySqlConnection connectionDb;

        public DatabaseConnector()
        {
            connectionDb = new MySqlConnection(connectionStr);
            connectionDb.Open();
        }

        ~DatabaseConnector()
        {
            connectionDb.Close();
        }

        public int CheckUser(string email, string password)
        {
            string sqlRequest = $"SELECT Id FROM Users WHERE Email = '{ email }' AND password = '{ password }'";
            MySqlDataReader reader = new MySqlCommand(sqlRequest, connectionDb).ExecuteReader();
            if (reader.Read())
            {
                int id = (int)reader[0];
                reader.Close();
                return id;
            }
            else
            {
                reader.Close();
                return -1;
            }
        }


        public int AddUser(string name, string email, string password)
        {
            string sqlRequest = $"SELECT Id FROM Users WHERE Email = '{ email }'";
            MySqlDataReader reader = new MySqlCommand(sqlRequest, connectionDb).ExecuteReader();
            if (reader.HasRows)
            {
                reader.Close();
                return 1;
            }
            reader.Close();
            if ((name != null) && (email != null) && (password != null) && (name.Length * email.Length * password.Length > 0))
            {
                sqlRequest = $"INSERT INTO Users (Name, Email, Password) VALUES ('{ name }', '{ email }', '{ password }')";
                MySqlCommand command = new MySqlCommand(sqlRequest, connectionDb);
                command.ExecuteNonQuery();
                return 0;
            }
            else
            {
                return 2;
            }
        }

        public int UpdateNote(Note note)
        {
            string sqlRequest = $"SELECT * FROM Notes WHERE Id = { note.Id }";
            if (note.Id <= 0)
            {
                sqlRequest = $"INSERT INTO Notes (Name, Data, Tags, LastEdit, UserId, IsDeleted) " +
                    $"VALUES ('{ note.Name }', '{ note.Data }', '{ note.Tags }', { note.LastEdit }, { note.UserId }, { note.IsDeleted })";
                MySqlCommand command = new MySqlCommand(sqlRequest, connectionDb);
                command.ExecuteNonQuery();
                sqlRequest = $"SELECT Id FROM Notes " +
                    $"WHERE Name = '{ note.Name }' AND Data = '{ note.Data }' AND LastEdit = { note.LastEdit } ORDER BY Id DESC;";
                command = new MySqlCommand(sqlRequest, connectionDb);
                return (int)command.ExecuteScalar();
            }
            else
            {
                sqlRequest = $"UPDATE Notes SET Name = '{ note.Name }', Data = '{ note.Data }', Tags = '{ note.Tags }', LastEdit = { note.LastEdit }, IsDeleted = { note.IsDeleted}  WHERE Id = { note.Id }";
                MySqlCommand command = new MySqlCommand(sqlRequest, connectionDb);
                command.ExecuteNonQuery();
                return 0;
            }
        }

        public ResponseAsk GetAllNotesDate(int userId)
        {
            string sqlRequest = $"SELECT Id, LastEdit FROM Notes WHERE UserId = { userId }";
            MySqlDataReader reader = new MySqlCommand(sqlRequest, connectionDb).ExecuteReader();
            List<int> ids = new List<int>();
            List<ulong> dates = new List<ulong>();
            while (reader.Read())
            {
                ids.Add((int)reader[0]);
                dates.Add((ulong)reader[1]);
            }
            reader.Close();
            return new ResponseAsk() { NoteId = ids.ToArray(), NoteDate = dates.ToArray(), Result = "OK", UserId = userId };
        }

        public Note GetNote(int id)
        {
            string sqlRequest = $"SELECT * FROM Notes WHERE Id = { id }";
            MySqlDataReader reader = new MySqlCommand(sqlRequest, connectionDb).ExecuteReader();
            if (reader.Read())
            {
                Note returnNote = new Note()
                {
                    Id = id,
                    Data = (string)reader[2],
                    Name = (string)reader[1],
                    LastEdit = (ulong)reader[4],
                    Tags = (string)reader[3],
                    UserId = (int)reader[6],
                    IsDeleted = (bool)reader[5]
                };
                reader.Close();
                return returnNote;
            }
            else
            {
                reader.Close();
                return null;
            }
        }
    }
}
