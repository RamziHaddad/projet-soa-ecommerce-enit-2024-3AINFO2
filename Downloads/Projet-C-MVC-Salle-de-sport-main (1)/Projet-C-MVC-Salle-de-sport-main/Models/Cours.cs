using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace ProjetC_MVCSalleSport.Models
{
    public class Cours
    {
        [Key]
        public int id_cours { get; set; }
        public string nom { get; set; }
        public DateTime date { get; set; }
        public string heure { get; set; }
        public string type { get; set; }
<<<<<<< HEAD
        public string NomCoach { get; set; }
=======
>>>>>>> 9302fff822ecb79e10b2c0f6ae0ee2f4cc840620
        public ICollection<Membre> membres { get; set; }
        public Coach Coaches { get; set; }
        public int id_coach { get; set; }
        public ICollection<Admin> admin { get; set; }
    }
}
