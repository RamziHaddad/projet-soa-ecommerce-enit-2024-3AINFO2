using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ProjetC_MVCSalleSport.Models
{
    [Table("membre")]
    public class Membre
    {
        public Membre()
        {
        }
        [Key]
        public int id_membre { get; set; }

        public String nom { get; set; }

        public String prenom { get; set; }

        public int telephone { get; set; }

        public String motDePasse { get; set; }

        public String mail { get; set; }
        public ICollection<Abonnement> Abonnements { get; set; }


        public ICollection<Progrés> Progres { get; set; }
        public ICollection<Admin> admins { get; set; }

        public ICollection<Cours> Cours { get; set; }





    }
}