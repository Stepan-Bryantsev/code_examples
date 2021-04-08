using System;
using System.Text;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Text.Json;
using ServerClassesLibrary;

namespace Server
{
    class Program
    {
        const int port = 3007;

        static void Main()
        {
            Server server = new Server();
            server.StartServer(port);
            Console.ReadLine();
        }
    }

    class Server
    {
        static DatabaseConnector DB = new DatabaseConnector();

        public Server()
        {
            int MaxThreadsCount = Environment.ProcessorCount * 4;
            ThreadPool.SetMaxThreads(MaxThreadsCount, MaxThreadsCount);
            ThreadPool.SetMinThreads(2, 2);
        }

        public void StartServer(int port)
        {
            TcpListener server = new TcpListener(IPAddress.Parse("10.0.0.4"), port);
            try
            {
                server.Start();
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                return;
            }
            while (true)
            {
                Console.WriteLine("Waiting for connections...");
                ThreadPool.QueueUserWorkItem(new WaitCallback(ClientThread), server.AcceptTcpClient());
            }
        }

        void ClientThread(Object clientObj)
        {
            TcpClient client = (TcpClient)clientObj;
            NetworkStream stream;
            try
            {
                stream = client.GetStream();
            }
            catch (Exception ex)
            {
                Console.WriteLine(ex.Message);
                client.Close();
                return;
            }
            Console.WriteLine("Client found");
            Request request = null;
            string requestString = AcceptMessage(stream);
            try
            {
                request = JsonSerializer.Deserialize<Request>(requestString);
                if (request.Action == "ask")
                {
                    request = JsonSerializer.Deserialize<RequestAsk>(requestString);
                }
                else if (request.Action == "create")
                {
                    request = JsonSerializer.Deserialize<RequestCreateUser>(requestString);
                }
            }
            catch
            {
                Console.WriteLine("Wrong request");
                stream.Close();
                client.Close();
                return;
            }

            if (request.Action == "ask")
            {
                int userId;
                try
                {
                    userId = DB.CheckUser(((RequestAsk)request).Email, ((RequestAsk)request).Password);
                }
                catch
                {
                    stream.Close();
                    client.Close();
                    return;
                }
                Console.WriteLine(userId + "ask");
                if (userId == -1)
                {
                    string returnMessage = JsonSerializer.Serialize(new ResponseAsk() { Result = "401" });
                    stream.Write(Encoding.Default.GetBytes(returnMessage), 0, returnMessage.Length);
                    stream.Close();
                    client.Close();
                    return;
                }
                string responseString = JsonSerializer.Serialize(DB.GetAllNotesDate(userId));
                try
                {
                    stream.Write(Encoding.Default.GetBytes(responseString), 0, responseString.Length);
                }
                catch
                {
                    stream.Close();
                    client.Close();
                    return;
                }
                ClientThreadUpdateNotes(client, stream, userId);
            }
            else if (request.Action == "create")
            {
                CreateUser(client, stream, (RequestCreateUser)request);
            }
            else
            {
                stream.Close();
                client.Close();
                return;
            }
        }

        private void ClientThreadUpdateNotes(TcpClient client, NetworkStream stream, int userId)
        {
            while (client.Connected)
            {
                string requestString = AcceptMessage(stream);
                RequestUpdate request;
                try
                {
                    request = JsonSerializer.Deserialize<RequestUpdate>(requestString);
                }
                catch
                {
                    stream.Close();
                    client.Close();
                    return;
                }
                string responseString;
                if (request.Action == "get")
                {
                    Note note = DB.GetNote(request.NoteId);
                    responseString = JsonSerializer.Serialize(new ResponeAction() { Result = "OK", Note = note });
                }
                else if (request.Action == "update")
                {
                    request.Note.Data.Replace("'", " ");
                    request.Note.Name.Replace("'", " ");
                    request.Note.Tags.Replace("'", " ");
                    int result = DB.UpdateNote(request.Note);
                    responseString = JsonSerializer.Serialize(new ResponeAction() { Result = "OK", NoteId = result, Note = null });
                }
                else
                {
                    responseString = "403";
                }
                try
                {
                    stream.Write(Encoding.Default.GetBytes(responseString), 0, responseString.Length);
                }
                catch
                {
                    stream.Close();
                    client.Close();
                    return;
                }
            }
            stream.Close();
            client.Close();
        }

        private void CreateUser(TcpClient client, NetworkStream stream, RequestCreateUser requestAdd)
        {
            try
            {
                int result = DB.AddUser(requestAdd.Name, requestAdd.Email, requestAdd.Password);
                string returnMessage = JsonSerializer.Serialize(new Response() { Result = result == 1 ? "User with this email already exists" : result == 2 ? "Wrong request data" : "OK" });
                stream.Write(Encoding.Default.GetBytes(returnMessage), 0, returnMessage.Length);
            }
            finally
            {
                stream.Close();
                client.Close();
            }
        }

        private string AcceptMessage(NetworkStream stream)
        {
            byte[] buffer = new byte[1024];
            int data_length;
            StringBuilder data = new StringBuilder();
            do
            {
                try
                {
                    data_length = stream.Read(buffer, 0, buffer.Length);
                }
                catch
                {
                    Console.WriteLine("Wrong request");
                    return null;
                }
                data.Append(Encoding.Default.GetString(buffer, 0, data_length));
            }
            while (stream.DataAvailable);
            Console.WriteLine("Request: " + data.ToString());
            return data.ToString();
        }
    }
}
