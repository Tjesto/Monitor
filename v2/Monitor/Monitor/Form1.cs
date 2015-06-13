using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.IO;

namespace Monitor
{
    public partial class Form1 : Form
    {
        private string[] firstTerms = new string[5] { "wrzesień", "październik", "listopad", "grudzień", "styczeń" };
        private string[] secondTerms = new string[5] { "luty", "marzec", "kwiecień", "maj", "czerwiec" };
        private string[] classesFilesPath;
        private string[] classSchoolYears;

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            classesFilesPath = Directory.GetFiles(@".\data\classes", "*.clsf");
            comboBox1.Items.Clear();
            foreach (string classfile in classesFilesPath)
            {
                string classfileV = classfile.Remove(0, @".\data\classes\".Length);
                classfileV = classfileV.Split(new char[] { '.' })[0];
                comboBox1.Items.Add(classfileV);
                comboBox6.Items.Add(classfileV);
            }
        }

        private void tabPage1_Click(object sender, EventArgs e)
        {
            //set defaults for semestr
        }

        private void tabPage2_Click(object sender, EventArgs e)
        {
            //set default for overall
        }

        private void zakończToolStripMenuItem_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void comboBox1_SelectedIndexChanged(object sender, EventArgs e)
        {
            comboBox2.Text = DateTime.Now.Month >= 9 || DateTime.Now.Month == 1 ? "Pierwszy" : "Drugi";
            classSchoolYears = parseSchoolYearsFiles(classesFilesPath[comboBox1.Items.IndexOf(comboBox1.Text)]);
            int i = 0;
            int term = comboBox2.Text.Equals("Pierwszy") ? 0 : 1;
            foreach (string s in classSchoolYears) 
            {
                if (s.Split(new char[] { '/' })[term].Equals(DateTime.Now.Year.ToString()))
                {
                    comboBox3.Text = s;
                    break;
                }
                i++;
            }
            if (comboBox3.Text == null || comboBox3.Text.Equals(""))
            {
                comboBox3.Text = classSchoolYears[classSchoolYears.Length - 1];
            }
            comboBox6.Text = comboBox1.Text;
        }

        private string[] parseSchoolYearsFiles(string p)
        {
            StreamReader r = new StreamReader(p);
            comboBox3.Items.Clear();            
            List<string> res = new List<string>();
            while (!r.EndOfStream)
            {
                string line = r.ReadLine();
                if (line.Contains('/'))
                {
                    comboBox3.Items.Add(line);
                    res.Add(line);
                }
            }
            return res.ToArray();
        }

        private void dataGridView1_CellContentClick(object sender, DataGridViewCellEventArgs e)
        {

        }

        private void comboBox2_SelectedIndexChanged(object sender, EventArgs e)
        {
            int i = 0;
            dataGridView1.Rows.Clear();
            foreach (DataGridViewColumn c in dataGridView1.Columns)
            {
                if (!c.ReadOnly) {
                    c.HeaderText = comboBox2.Text.Equals("Pierwszy") ? getFirstTermName(i++) : getSecondTermName(i++);                    
                }
            }
            parseData();
            comboBox5.Text = comboBox2.Text;
        }

        private String getFirstTermName(int i)
        {
            return firstTerms[i];
        }

        private String getSecondTermName(int i)
        {
            return secondTerms[i];
        }

        private void comboBox3_SelectedIndexChanged(object sender, EventArgs e)
        {
            dataGridView1.Rows.Clear();
            parseData();
            comboBox4.Text = comboBox3.Text;
        }

        private void parseData()
        {
            string classname = classesFilesPath[comboBox1.Items.IndexOf(comboBox1.Text)];
            StreamReader r = new StreamReader(classname);
            string year = comboBox3.Text;
            bool read = false;
            while (!r.EndOfStream)
            {
                string line = r.ReadLine();
                if (line.Equals(";"))
                {
                    read = false;
                    continue;
                }
                if (!read) {
                    read = line.Equals(year);
                    continue;
                }
                string[] row = new string[dataGridView1.ColumnCount];
                string[] data = line.Split(new char[]{';'});
                int cell = 0;
                for (int i = 0; i < data.Length; i++)
                {                    
                    if (cell < dataGridView1.ColumnCount) {
                        string val = "";
                        if (comboBox2.Text.Equals("Pierwszy"))
                        {
                            if (i == 0 || i >= 4 && i <= 8)
                            {
                                row[cell++] = data[i];
                            }                            
                        }
                        else
                        {
                            if (i == 0 || i >= 9)
                            {
                                row[cell++] = data[i];
                            }
                            
                        }                        
                    }
                }               
                dataGridView1.Rows.Add(row);
                DataGridViewRow curRow = dataGridView1.Rows[dataGridView1.Rows.Count-2];
                curRow.Cells[dataGridView1.ColumnCount - 1].Value = sum(curRow);
            }
        }

        private void dataGridView1_CellValueChanged(object sender, DataGridViewCellEventArgs e)
        {
            if (e.RowIndex < 0 || e.RowIndex >= dataGridView1.Rows.Count)
            {
                return;
            }
            DataGridViewRow currentRow = dataGridView1.Rows[e.RowIndex];
            DataGridViewCell totalCell = currentRow.Cells[dataGridView1.ColumnCount-1];
            
            totalCell.Value = sum(currentRow);
            
        }

        private int sum(DataGridViewRow row)
        {
            int sum = 0;
            foreach (DataGridViewCell c in row.Cells)
            {
                if (!c.ReadOnly)
                {
                    sum += c.Value != null ? int.Parse(c.Value.ToString()) : 0;
                }
            }
            return sum;
        }

        private void comboBox6_SelectedIndexChanged(object sender, EventArgs e)
        {
            comboBox1.Text = comboBox6.Text;
        }

        private void comboBox5_SelectedIndexChanged(object sender, EventArgs e)
        {
            comboBox2.Text = comboBox5.Text;
        }

        private void comboBox4_SelectedIndexChanged(object sender, EventArgs e)
        {
            comboBox3.Text = comboBox4.Text;
        }
    }

}
