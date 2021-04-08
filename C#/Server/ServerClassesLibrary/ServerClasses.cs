using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ServerClassesLibrary
{
    public class Note
    {
        public int Id { get; set; }
        public int UserId { get; set; }
        public string Name { get; set; }
        public string Data { get; set; }
        public string Tags { get; set; }
        public ulong LastEdit { get; set; }
        public bool IsDeleted { get; set; }
    }

    public class Request
    {
        public string Action { get; set; }
    }

    public class RequestAsk : Request
    {
        public string Email { get; set; }
        public string Password { get; set; }
    }

    public class RequestUpdate : Request
    {
        public int NoteId { get; set; }
        public Note Note { get; set; }
    }

    public class RequestCreateUser : Request
    {
        public string Name { get; set; }
        public string Email { get; set; }
        public string Password { get; set; }
    }

    public class Response
    {
        public string Result { get; set; }
    }

    public class ResponseAsk : Response
    {
        public int UserId { get; set; }
        public int[] NoteId { get; set; }
        public ulong[] NoteDate { get; set; }
    }

    public class ResponeAction : Response
    {
        public int NoteId { get; set; }
        public Note Note { get; set; }
    }
}
