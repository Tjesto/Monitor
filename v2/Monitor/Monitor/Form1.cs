using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using System.Threading;
using System.IO;

namespace Monitor
{
    public partial class Form1 : Form
    {
        private string[] firstTerms = new string[5] { "wrzesień", "październik", "listopad", "grudzień", "styczeń" };
        private string[] secondTerms = new string[5] { "luty", "marzec", "kwiecień", "maj", "czerwiec" };
        private string[] classesFilesPath;
        private string[] classSchoolYears;
        private long last;

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
            List<string> res = new List<string>();
            using (StreamReader r = new StreamReader(p))
            {
                comboBox3.Items.Clear();
                comboBox4.Items.Clear(); 
                while (!r.EndOfStream)
                {
                    string line = r.ReadLine();
                    if (line.Contains('/'))
                    {
                        comboBox3.Items.Add(line);
                        comboBox4.Items.Add(line);
                        res.Add(line);
                    }
                }
                r.Close();
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
            using (StreamReader r = new StreamReader(classname))
            {
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
                    if (!read)
                    {
                        read = line.Equals(year);
                        continue;
                    }
                    string[] row = new string[dataGridView1.ColumnCount];
                    string[] data = line.Split(new char[] { ';' });
                    int cell = 0;
                    for (int i = 0; i < data.Length; i++)
                    {
                        if (cell < dataGridView1.ColumnCount)
                        {
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
                    DataGridViewRow curRow = dataGridView1.Rows[dataGridView1.Rows.Count - 2];
                    curRow.Cells[dataGridView1.ColumnCount - 1].Value = sum(curRow);
                }
                r.Close();
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
            //save();
        }

        private void save()
        {
            long now = DateTime.Now.Ticks;
            if (last + 10000 > now)
            {
                return;
            }
            last = now;
            List<string> current = new List<string>();
            FileStream s = File.Open(classesFilesPath[comboBox1.Items.IndexOf(comboBox1.Text)],FileMode.OpenOrCreate, FileAccess.ReadWrite);
            using (StreamReader r = new StreamReader(s)/*new StreamReader(classesFilesPath[comboBox1.Items.IndexOf(comboBox1.Text)])*/) {
            while (!r.EndOfStream)
            {
                current.Add(r.ReadLine());
            }
            r.Close();
            }            
            using (StreamWriter w = new StreamWriter(s))
            {
                bool read = false;
                int i = 0;
                foreach (string line in current)
                {
                    if (line.Equals(";"))
                    {
                        read = false;
                        w.WriteLine(line);
                        continue;
                    }
                    if (!read)
                    {
                        read = line.Equals(comboBox3.Text);
                        w.WriteLine(line);
                        continue;
                    }
                    StringBuilder b = new StringBuilder();
                    int cellNum = 1;
                    for (int k = 0; k < line.Split(new char[] { ';' }).Length; k++)
                    {
                        if (k < 4)
                        {
                            b.Append(line.Split(new char[] { ';' })[k]).Append(';');
                        }
                        else if (k >= 4 && k < 9)
                        {
                            if (comboBox2.Text.Equals("Pierwszy"))
                            {
                                b.Append(dataGridView1.Rows[i].Cells[cellNum++].Value).Append(';');
                            }
                            else
                            {
                                b.Append(line.Split(new char[] { ';' })[k]).Append(';');
                            }
                        }
                        else if (k >= 9)
                        {
                            if (comboBox2.Text.Equals("Drugi"))
                            {
                                b.Append(dataGridView1.Rows[i].Cells[cellNum++].Value).Append(';');
                            }
                            else
                            {
                                b.Append(line.Split(new char[] { ';' })[k]).Append(';');
                            }
                        }
                    }
                    w.WriteLine(b.ToString());
                }
                w.Close();
            }
        }

        private int sum(DataGridViewRow row)
        {
            int sum = 0;
            foreach (DataGridViewCell c in row.Cells)
            {
                if (!c.ReadOnly)
                {
                    sum += c.Value != null && !c.Value.Equals("")? int.Parse(c.Value.ToString()) : 0;
                }
            }
            return sum;
        }

        private int sumOverall(DataGridViewRow row)
        {
            int sum = 0;
            foreach (DataGridViewCell c in row.Cells)
            {
                if (c.ColumnIndex > 0 && c.ColumnIndex < 4)
                {
                    sum += c.Value != null && !c.Value.Equals("") ? int.Parse(c.Value.ToString()) : 0;
                }
            }
            return sum;
        }

        private void comboBox6_SelectedIndexChanged(object sender, EventArgs e)
        {
            comboBox1.Text = comboBox6.Text;
            comboBox1_SelectedIndexChanged(sender, e);
        }

        private void comboBox5_SelectedIndexChanged(object sender, EventArgs e)
        {
            dataGridView2.Rows.Clear();
            comboBox2.Text = comboBox5.Text;
            parseOverallData();
        }

        private void parseOverallData()
        {
            if (comboBox6.Text == null || comboBox6.Text.Equals(""))
            {
                return;
            }
            string classname = classesFilesPath[comboBox6.Items.IndexOf(comboBox6.Text)];
            using (StreamReader r = new StreamReader(classname))
            {
                string year = comboBox4.Text;
                bool read = false;
                int row = 0;
                int cell = -1;
                while (!r.EndOfStream)
                {
                    string line = r.ReadLine();
                    if (line.Equals(";"))
                    {
                        read = false;
                        continue;
                    }
                    if (!read)
                    {
                        foreach (DataGridViewColumn column in dataGridView2.Columns)
                        {
                            read |= column.HeaderText.Equals(line);
                            if (read)
                            {
                                cell = column.Index;
                                year = column.HeaderText;
                                break;
                            }
                        }
                        continue;
                    }
                    int dataSum = 0;
                    string[] data = line.Split(new char[] { ';' });
                    for (int i = 0; i < data.Length; i++)
                    {
                        if (cell < dataGridView2.ColumnCount)
                        {                            
                            if (i == 0)
                            {

                            }
                            if (comboBox5.Text.Equals("Pierwszy"))
                            {
                                if (i >= 4 && i <= 8)
                                {
                                    dataSum += int.Parse(data[i]);
                                }
                            }
                            else
                            {
                                if (i >= 4 && i < 14)
                                {
                                    dataSum += int.Parse(data[i]);
                                }

                            }
                        }
                    }
                    bool exists = false;
                    foreach (DataGridViewRow roow in dataGridView2.Rows)
                    {
                        if (roow.Cells[0].Value != null && roow.Cells[0].Value.Equals(data[0]))
                        {
                            exists = true;
                            row = roow.Index;
                            break;
                        }

                    }
                    if (!exists)
                    {
                        dataGridView2.Rows.Add(data[0], "", "", "", "", (comboBox5.Text.Equals("Pierwszy") ? data[1] : (int.Parse(data[1]) + (int.Parse(data[2]))).ToString()), data[3]);
                    }
                    DataGridViewRow curRow = dataGridView2.Rows[row++];
                    if (int.Parse(year.Split(new char[]{ '/' })[0]) <= int.Parse(comboBox4.Text.Split(new char[]{ '/' })[0])) {
                        curRow.Cells[cell].Value = dataSum;
                    }
                    curRow.Cells[dataGridView2.ColumnCount - 3].Value = sumOverall(curRow);                    
                }
                r.Close();
            }
        }

        private void comboBox4_SelectedIndexChanged(object sender, EventArgs e)
        {
            dataGridView2.Rows.Clear();
            comboBox3.Text = comboBox4.Text;
            string[] years = getYears(comboBox4.Text);
            int header = 0;
            foreach (DataGridViewColumn c in dataGridView2.Columns)
            {
                int index = c.Index;
                if (index > 0 && index < 4)
                {
                    c.HeaderText = years[header++];
                }
            }
            parseOverallData();
        }

        private string[] getYears(string p)
        {
            string[] res = new string[3];
            int refIndex = -1;
            for (int i = 0; i < classSchoolYears.Length; i++)
            {
                string s = classSchoolYears[i];
                if (s.Equals(p))
                {
                    refIndex = i;
                    break;
                }
            }
            if (refIndex < 0 || refIndex > classSchoolYears.Length)
            {
                throw new ArgumentException("No such year");
            }
            for (int i = 0; i < 3; i++)
            {
                int index = refIndex - ((refIndex % 3) - i);
                res[i] = index < classSchoolYears.Length ? classSchoolYears[index] :
                    nextYear(res[i - 1].Split(new char[] { '/' })[1]);
            }
            return res;
        }

        private string nextYear(string p)
        {
            int year = int.Parse(p);
            return year + "/" + (year + 1);
        }


    }

}
