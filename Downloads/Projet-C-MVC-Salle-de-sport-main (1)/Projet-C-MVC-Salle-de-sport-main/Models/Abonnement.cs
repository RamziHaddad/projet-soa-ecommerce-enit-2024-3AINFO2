using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ProjetC_MVCSalleSport.Models
{
    public class Abonnement
    {
        public Abonnement()
        {
        }

        [Key]
        public int id_ab { get; set; }

        public DateTime dateDebut { get; set; }

        public DateTime dateFin { get; set; }

        public bool payement { get; set; }

        public string typeCours { get; set; }

        [ForeignKey("Membre")]
        public int id_membre { get; set; }

        public Membre Membre { get; set; }
    }
}
