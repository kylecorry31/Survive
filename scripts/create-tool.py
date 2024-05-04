# Step 1: Ask for the tool name
# Step 2: Create a new directory with the tool name in the tools directory
# Step 3: Create a ui directory in the new tool directory
# Step 4: Create a new file in the ui directory using the template for the tool fragment
# Step 5: Create a new file in the new tool directory using the template for the tool registration
# Step 6: Create a new file in the layouts directory using the template for the tool layout

import os
import sys
import shutil

script_path = os.path.dirname(os.path.realpath(__file__))

tool_name = input("Enter the name of the tool: ")
package_name =  tool_name.replace(' ', '').lower()
class_name = tool_name.replace(' ', '')

# Step 2
tool_dir = os.path.join(script_path, '../src/main/java/com/kylecorry/trail_sense/tools', package_name)
os.makedirs(tool_dir)

# Step 3
ui_dir = os.path.join(tool_dir, 'ui')
os.makedirs(ui_dir)

# Step 4
fragment_class = f"""package com.kylecorry.trail_sense.tools.{package_name};

import com.kylecorry.trail_sense.databinding.FragmentTool{class_name}Binding
import com.kylecorry.andromeda.fragments.BoundFragment

class Tool{class_name}Fragment : BoundFragment<FragmentTool{class_name}Binding>() {'{'}

    override fun generateBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTool{class_name}Binding {'{'}
        return FragmentTool{class_name}Binding.inflate(layoutInflater, container, false)
    {'}'}

{'}'}
"""
with open(os.path.join(ui_dir, f'Tool{class_name}Fragment.kt'), 'w') as f:
    f.write(fragment_class)

# Step 5
registration_class = f"""package com.kylecorry.trail_sense.tools.{package_name}

import android.content.Context
import com.kylecorry.trail_sense.R
import com.kylecorry.trail_sense.tools.tools.infrastructure.Tool
import com.kylecorry.trail_sense.tools.tools.infrastructure.ToolCategory
import com.kylecorry.trail_sense.tools.tools.infrastructure.ToolRegistration
import com.kylecorry.trail_sense.tools.tools.infrastructure.Tools

object {class_name}ToolRegistration : ToolRegistration {'{'}
    override fun getTool(context: Context): Tool {'{'}
        return Tool(
            Tools.{tool_name.replace(' ', '_').upper()},
            "{tool_name}",
            R.drawable.ic_info,
            R.id.mirrorCameraFragment,
            ToolCategory.Other
        )
    {'}'}
{'}'}
"""

with open(os.path.join(tool_dir, f'{class_name}ToolRegistration.kt'), 'w') as f:
    f.write(registration_class)

    