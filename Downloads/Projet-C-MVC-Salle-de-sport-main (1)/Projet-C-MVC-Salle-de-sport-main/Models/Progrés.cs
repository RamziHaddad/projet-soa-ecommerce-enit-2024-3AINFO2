using System;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

namespace ProjetC_MVCSalleSport.Models
{
    [Table("progres")]
    public class Progrés
    {
        [Key]
        public int id_progres { get; set; }
        public float poids { get; set; }
        public float imc { get; set; }
        public DateTime date { get; set; }
        [ForeignKey("Membre")]
        public int id_membre { get; set; }

        public Membre Membre { get; set; }
    }
}
