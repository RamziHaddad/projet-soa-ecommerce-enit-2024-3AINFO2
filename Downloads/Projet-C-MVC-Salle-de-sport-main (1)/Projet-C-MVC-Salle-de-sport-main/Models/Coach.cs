using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ProjetC_MVCSalleSport.Models
{
    [Table("coach")]
    public class Coach
    {
        [Key]
        public int id_coach { get; set; }
        public string nom { get; set; }
        public string prenom { get; set; }
        public int telephone { get; set; }
        public string motDePasse { get; set; }
        public string mail { get; set; }

        public ICollection<Admin> admins { get; set; }
        public ICollection<Cours> cours { get; set; }

    }
}
