using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ProjetC_MVCSalleSport.Models
{
    [Table("admin")]
    public class Admin
    {

        public Admin()
        {
        }
        [Key]
        public int id_admin { get; set; }

        public string nom { get; set; }

        public string prenom { get; set; }

        public string motDePasse { get; set; }

        public string mail { get; set; }

        // Si vous souhaitez stocker des références vers d'autres entités, utilisez des collections
        public ICollection<Membre> membres { get; set; }

        public ICollection<Cours> cours { get; set; }

        public ICollection<Coach> coach { get; set; }
    }
}
